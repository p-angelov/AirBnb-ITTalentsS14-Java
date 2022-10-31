package com.ittalents.airbnb.services;
import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.*;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyCharacteristicsDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyEditDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyPriceDto;
import com.ittalents.airbnb.model.dto.reviewDtos.ReviewResponseDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserResponseDto;
import com.ittalents.airbnb.model.entity.*;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.exceptions.UnauthorizedException;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PropertyService extends AbstractService {

    private static final String PROPERTY_PHOTOS_PATH = "photos/properties_photos";
    private static final int SIZE_OF_PAGE = 8;

    //method to generate long representation of property-extras,when the long is turned to its binary representation, each bit will show,whether this extra is in property or not.
    public long generateLongFromExtras(PropertyCreationDto dto) {
        long binaryExtras = 0;
        binaryExtras += dto.isHasWifi() ? Math.pow(2, 0) : 0;
        binaryExtras += dto.isHasBalcony() ? Math.pow(2, 1) : 0;
        binaryExtras += dto.isHasAirConditioning() ? Math.pow(2, 2) : 0;//
        binaryExtras += dto.isHasWashingMachine() ? Math.pow(2, 3) : 0;
        binaryExtras += dto.isHasDishWasher() ? Math.pow(2, 4) : 0;
        binaryExtras += dto.isHasBabyCrib() ? Math.pow(2, 5) : 0;
        binaryExtras += dto.isHasYard() ? Math.pow(2, 6) : 0;
        binaryExtras += dto.isHasParking() ? Math.pow(2, 7) : 0;
        binaryExtras += dto.isHasKitchen() ? Math.pow(2, 8) : 0;
        binaryExtras += dto.isHasTV() ? Math.pow(2, 9) : 0;
        binaryExtras += dto.isHasChildrenPlayground() ? Math.pow(2, 10) : 0;
        return binaryExtras;
    }

    public PropertyCreationDto add(PropertyCreationDto dto, long id) {
        if (!getUserById(id).isHost()) {
            throw new BadRequestException("User isn't a host!");
        }
        Property p = modelMapper.map(dto, Property.class);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!")));
        long binaryExtras = generateLongFromExtras(dto);
        Address a = new Address(); //todo use mapper
        a.setCountry(dto.getCountry());
        a.setCity(dto.getCity());
        a.setStreet(dto.getStreet());
        a.setNumber(dto.getNumber());
        p.setAddress(a);
        if (dto.getPropertyPhotos() != null) {
            for (String url : dto.getPropertyPhotos()) {
                Photo propertyPhoto = new Photo();
                propertyPhoto.setPhotoUrl(url);
                p.getPropertyPhotos().add(propertyPhoto);
                photoRepository.save(propertyPhoto);
            }
        }
        p.setExtras(binaryExtras);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found!")));
        a.setProperty(p);
        propertyRepository.save(p);
        dto.setId(p.getId());
        dto.setHost(modelMapper.map(p, UserResponseDto.class));
        return dto;
    }

    public List<GeneralPropertyResponseDto> getUserProperties(long id) {
        User u = getUserById(id);
        if (!u.isHost()) {
            throw new BadRequestException("The user is not host");
        }
        List<GeneralPropertyResponseDto> responseDto = new ArrayList<>();
        //responseDto = u.getProperties().stream().map(property -> modelMapper.map(property, GeneralPropertyResponseDto.class) ).collect(Collectors.toList());
        for (Property property : u.getProperties()) {
            GeneralPropertyResponseDto dto;
            dto = modelMapper.map(property, GeneralPropertyResponseDto.class);
            putExtras(dto, property.getExtras());
            responseDto.add(modelMapper.map(property, GeneralPropertyResponseDto.class));
        }
        return responseDto;
    }

    public PropertyResponseDto edit(long pid, PropertyEditDto dto, long userId) {
        Property property = propertyRepository.getReferenceById(pid);
        User u = getUserById(userId);
        List<Property> userProperties = u.getProperties();
        if (userProperties.isEmpty() || userProperties == null) {
            throw new BadRequestException("This user doesn't have any properties");
        }
        if (property.getHost().getId() != userId) {
            throw new BadRequestException("User isnt the host of this property");
        }
        property = propertyRepository.findById(pid).orElseThrow(() -> new NotFoundException("There is no such property"));
        property = modelMapper.map(dto, Property.class);
        property.setId(pid);
        property.setAddress(propertyRepository.findById(pid).get().getAddress());
        property.setHost(u);
        property.setExtras(generateLongFromExtras(modelMapper.map(dto, PropertyCreationDto.class)));
        propertyRepository.save(property);
        PropertyResponseDto respDto = modelMapper.map(property, PropertyResponseDto.class);
        respDto.setAddress(property.getAddress());
        respDto.setPropertyPhotos(propertyRepository.findById(pid).get().getPropertyPhotos());
        return respDto;
    }

    public GeneralPropertyResponseDto getPropertyById(long id) {
        Property p = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("Property not found!"));
        GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
        putExtras(dto, p.getExtras());
        return dto;
    }

    public List<GeneralPropertyResponseDto> findAll() {
        List<GeneralPropertyResponseDto> res = new ArrayList<>();
        for (Property p : propertyRepository.findAll()) {
            GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
            long extras = p.getExtras();
            putExtras(dto, extras);
            res.add(dto);
        }
        return res;
    }

    @SneakyThrows
    public PhotoDto uploadPhoto(long id, MultipartFile file) {
        validatePhoto(file);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String photoName = System.nanoTime() + "." + extension;
        Files.copy(file.getInputStream(), new File(PROPERTY_PHOTOS_PATH + File.separator + photoName).toPath());
        Optional<Property> p = propertyRepository.findById(id);
        Photo image = new Photo();

        if (p.isPresent()) {
            image.setPhotoUrl(photoName);
            image.setProperty(p.get());
            p.get().getPropertyPhotos().add(image);

            photoRepository.save(image);
        } else {
            throw new NotFoundException("Property not found! Photo upload failed!");
        }

        PhotoDto dto = modelMapper.map(image, PhotoDto.class);

        return dto;
    }


//logic to generate extras from binary representation of extras in db
    public void putExtras(GeneralPropertyResponseDto dto, long extras) {
        for (int i = 0; i <= 10; i++) {
            int num = (int) Math.pow(2, i);//num is the extra,representing i-th extra,for example i=3 and fourth extra is Washing Machine
            if ((extras & num) > 0) {//from previous example, num is 8(1000),if we have property with wifi and Washing machine extras would be 9(1001), so bitwise AND will return number greater than zero,otherwise & will return zero
                switch (i) {
                    case 0:
                        dto.setHasWifi(true);
                        break;
                    case 1:
                        dto.setHasBalcony(true);
                        break;
                    case 2:
                        dto.setHasAirConditioning(true);
                        break;
                    case 3:
                        dto.setHasWashingMachine(true);
                        break;
                    case 4:
                        dto.setHasDishWasher(true);
                        break;
                    case 5:
                        dto.setHasBabyCrib(true);
                        break;
                    case 6:
                        dto.setHasYard(true);
                        break;
                    case 7:
                        dto.setHasParking(true);
                        break;
                    case 8:
                        dto.setHasKitchen(true);
                        break;
                    case 9:
                        dto.setHasTV(true);
                        break;
                    case 10:
                        dto.setHasChildrenPlayground(true);
                        break;

                }
            }
        }
    }

    public PhotoDto deletePhotoById(HttpServletRequest request, long photoId, long pid, long uid) {
        Optional<Photo> opt = photoRepository.findById(photoId);
        if (opt.isPresent()) {
            if (uid != propertyRepository.getReferenceById(pid).getHost().getId()) {
                throw new UnauthorizedException("Photo could not be deleted from property which does not belong to the logged user!");
            }
            PhotoDto response = modelMapper.map(opt, PhotoDto.class);
            photoRepository.deleteById(photoId);
            return response;
        } else {
            throw new NotFoundException("Photo not found!");
        }
    }

    public PropertyResponseDto remove(long pid) {
        Property p = getPropertyByIdAs(pid);
        PropertyResponseDto dto = modelMapper.map(p, PropertyResponseDto.class);
        propertyRepository.deletePropertyById(pid);
        return dto;
    }

    public PageDto filterByType(String typeName, long pageIdx) {
        String[] type = typeName.split("_");
        typeName = "";
        typeName = typeName.concat(type[0]);
        typeName = typeName.concat(" ");
        typeName = typeName.concat(type[1]);
        List<Property> propertiesByType = propertyPagingRepository.findAllByType(typeName, PageRequest.of(0, 8));
        List<PagePropertyDto> pagePropertyDto = getPagePropertyDtos(propertiesByType);
        PageDto returnDto = new PageDto();
        returnDto.setProperties(pagePropertyDto);
        returnDto.setCurrentPage((int) pageIdx);
        returnDto.setTotalItems(propertyRepository.findAllByType(typeName).size());
        returnDto.setTotalPages(returnDto.getTotalItems() / SIZE_OF_PAGE);
        return returnDto;
    }

    public PageDto makePage(List<Property> allProperties, int pageIdx) {
        PageDto returnDto = new PageDto();
        System.out.println(allProperties.size());
        returnDto.setProperties(allProperties.stream().map(property -> modelMapper.map(property,PagePropertyDto.class)).collect(Collectors.toList()));
        returnDto.setCurrentPage((int) pageIdx);
        returnDto.setTotalItems(allProperties.size());
        returnDto.setTotalPages(returnDto.getTotalItems() / SIZE_OF_PAGE);
        return returnDto;
    }
    public PageDto filterByPrice(PropertyPriceDto filter, long pageIdx) {
        List<Property> allProperties = propertyPagingRepository.findAllByPricePerNightBetween(filter.getMinPrice(), filter.getMaxPrice(), PageRequest.of((int) pageIdx, 8));
        return makePage(allProperties, (int) pageIdx);
    }
    public PageDto filterByCharacteristics(PropertyCharacteristicsDto dto, long pageIdx) {
        String city = dto.getCity();
        String country = dto.getCountry();
         long filterExtras = generateLongFromExtras(modelMapper.map(dto,PropertyCreationDto.class));
         PageRequest page = PageRequest.of((int) pageIdx, SIZE_OF_PAGE);
         List<Property> allProperties ;
        if (!city.isBlank() && !country.isBlank()) {
            allProperties = propertyDao.byCityCountryAndExtras((int)page.getOffset(),SIZE_OF_PAGE,city,country,(int)filterExtras,dto.getMaxGuests(),dto.getBeds(),dto.getBathrooms());
            System.out.println(allProperties.size());

        } else if (!dto.getCity().isBlank() && dto.getCountry().isBlank()) {
            allProperties = propertyDao.byCityAndExtras((int)page.getOffset(),SIZE_OF_PAGE,city,(int)filterExtras,dto.getMaxGuests(),dto.getBeds(),dto.getBathrooms());
        } else if (dto.getCity().isBlank() && !dto.getCountry().isBlank()) {
      allProperties = propertyDao.byCountryAndExtras((int)page.getOffset(),SIZE_OF_PAGE,country,(int)filterExtras,dto.getMaxGuests(),dto.getBeds(),dto.getBathrooms());

        } else {
           // System.out.println(page.getOffset());
            allProperties = propertyDao.byExtras((int)page.getOffset(),SIZE_OF_PAGE,(int)filterExtras,dto.getMaxGuests(),dto.getBeds(),dto.getBathrooms());
          //  System.out.println(allProperties.size());
        }
        if (allProperties.size() == 0) {
            throw new NotFoundException("No properties with such characteristics found!");
        }
        return makePage(allProperties, (int) pageIdx);
    }

    private List<PagePropertyDto> getPagePropertyDtos(List<Property> properties) {
        List<PagePropertyDto> dto = properties.stream().map(property -> modelMapper.map(property, PagePropertyDto.class)).collect(Collectors.toList());
        for (int i = 0; i < properties.size(); i++) {
            dto.get(i).setAddressDto(properties.get(i).getAddress());
        }
        return dto;
    }

    public List<ReviewResponseDto> getPropertyReviews(long id) {
        Property property = propertyRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("There is not property with such id");
        });
        List<Review> reviews = property.getReviews();
        List<ReviewResponseDto> dtoList = reviews.stream().map(review -> modelMapper.map(review, ReviewResponseDto.class)).collect(Collectors.toList());
        for (ReviewResponseDto dto : dtoList) {
            dto.setCommenterId(property.getHost().getId());
        }
        return dtoList;
    }
}
