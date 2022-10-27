package com.ittalents.airbnb.services;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.*;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyCharacteristicsDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyPriceDto;
import com.ittalents.airbnb.model.entity.Address;
import com.ittalents.airbnb.model.entity.Photo;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.exceptions.UnauthorizedException;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.weaver.patterns.BindingAnnotationTypePattern;
import org.springframework.data.domain.*;
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

    public long generateLongFromExtras(PropertyCreationDto dto) {
        long extrasB = 0;
        extrasB += dto.isHasWifi() ? Math.pow(2, 0) : 0;
        extrasB += dto.isHasBalcony() ? Math.pow(2, 1) : 0;
        extrasB += dto.isHasAirConditioning() ? Math.pow(2, 2) : 0;
        extrasB += dto.isHasWashingMachine() ? Math.pow(2, 3) : 0;
        extrasB += dto.isHasDishWasher() ? Math.pow(2, 4) : 0;
        extrasB += dto.isHasBabyCrib() ? Math.pow(2, 5) : 0;
        extrasB += dto.isHasYard() ? Math.pow(2, 6) : 0;
        extrasB += dto.isHasParking() ? Math.pow(2, 7) : 0;
        extrasB += dto.isHasKitchen() ? Math.pow(2, 8) : 0;
        extrasB += dto.isHasTV() ? Math.pow(2, 9) : 0;
        extrasB += dto.isHasChildrenPlayground() ? Math.pow(2, 10) : 0;
        return extrasB;
    }

    public PropertyCreationDto add(PropertyCreationDto dto, long id) {
        if (!getUserById(id).isHost()) {
            throw new BadRequestException("User isn't a host!");
        }
        Property p = modelMapper.map(dto, Property.class);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!")));
        long extrasB = generateLongFromExtras(dto);
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
        p.setExtras(extrasB);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found!")));
        a.setProperty(p);

        propertyRepository.save(p);
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

    public PropertyResponseDto edit(long pid, PropertyCreationDto dto, long userId) {
        Property property = propertyRepository.getReferenceById(pid);
        User u = getUserById(userId);
        List<Property> userProperties = u.getProperties();
        if (userProperties.isEmpty() || userProperties == null) {
            throw new BadRequestException("This user doesn't have any properties");
        }
        if (property.getHost().getId() != userId) {
            throw new BadRequestException("User isnt the host of this property");
        }
        property = modelMapper.map(dto, Property.class);
        property.setAddress(propertyRepository.findById(pid).get().getAddress());
        property.setHost(u);
        property.setExtras(generateLongFromExtras(dto));
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

    public void putExtras(GeneralPropertyResponseDto dto, long extras) {

        System.out.println(extras);
        for (int i = 0; i <= 10; i++) {
            int num = (int) Math.pow(2, i);
            if ((extras & num) > 0) {
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
       String[] type  =  typeName.split("_");
       typeName = "";
       typeName = typeName.concat(type[0]);
       typeName = typeName.concat(" ");
       typeName = typeName.concat(type[1]);
       List<Property> propertiesByType = propertyPagingRepository.findAllByType(typeName, PageRequest.of(0,8));
       List<PagePropertyDto> pagePropertyDto = getPagePropertyDtos(propertiesByType);
       PageDto returnDto = new PageDto();
       returnDto.setProperties(pagePropertyDto);
       returnDto.setCurrentPage((int) pageIdx);
       returnDto.setTotalItems(propertyRepository.findAllByType(typeName).size());
       returnDto.setTotalPages(returnDto.getTotalItems()/8);
       return returnDto;
    }

    public PageDto filterByPrice(PropertyPriceDto filter, long pageIdx) {
        List<Property> propertiesByPrice = propertyPagingRepository.findAllByPricePerNightBetween(filter.getMinPrice(), filter.getMaxPrice(), PageRequest.of((int) pageIdx,8));
        List<PagePropertyDto> pagePropertyDto = getPagePropertyDtos(propertiesByPrice);
        PageDto returnDto = new PageDto();
        returnDto.setProperties(pagePropertyDto);
        returnDto.setCurrentPage((int) pageIdx);
        returnDto.setTotalItems(propertyRepository.findAllByPricePerNightBetween(filter.getMinPrice(), filter.getMaxPrice()).size());
        returnDto.setTotalPages(returnDto.getTotalItems()/8);
        return returnDto;
    }

    public PageDto filterByCharacteristics(PropertyCharacteristicsDto dto, long pageIdx){
        String city = dto.getCity();
        String country = dto.getCountry();
        int maxGuests = dto.getMaxGuests();
        int bathrooms = dto.getBathrooms();
        int beds = dto.getBeds();
        List<Property> allProperties = new ArrayList<>();
        if (!city.isBlank() && !country.isBlank()){
             allProperties = propertyRepository.findPropertiesByAddress_CityAndAddress_Country(city, country);
        }
        else if (!dto.getCity().isBlank() && dto.getCountry().isBlank()){
            allProperties = propertyRepository.findPropertiesByAddress_City(city);
        }
        else if(dto.getCity().isBlank() && !dto.getCountry().isBlank()){
            allProperties = propertyRepository.findPropertiesByAddress_Country(country);

        }
        else{
            allProperties = propertyRepository.findAll();
        }
        if(maxGuests>0){
            allProperties = allProperties.stream().filter(property -> property.getMaxGuests()==maxGuests).collect(Collectors.toList());
        }
        if(beds>0){
            allProperties = allProperties.stream().filter(property -> property.getBeds()==beds).collect(Collectors.toList());
        }
        if(bathrooms >0){
            allProperties = allProperties.stream().filter(property -> property.getBathrooms()==bathrooms).collect(Collectors.toList());
        }
        Iterator<Property> it = allProperties.iterator();
        while(it.hasNext()) {
            Property property = it.next();
            GeneralPropertyResponseDto dtoExtras = modelMapper.map(property,GeneralPropertyResponseDto.class);
            putExtras(dtoExtras, property.getExtras());
            if(dtoExtras.isHasAirConditioning() == dto.isHasAirConditioning() && !dtoExtras.isHasAirConditioning()){
                it.remove();
            } else if (dtoExtras.isHasBabyCrib() == dto.isHasBabyCrib() && !dto.isHasBabyCrib()) {
                it.remove();
            }else if (dtoExtras.isHasBalcony() == dto.isHasBalcony() && !dto.isHasBalcony()) {
                it.remove();
            }else if (dtoExtras.isHasKitchen() == dto.isHasKitchen() && !dto.isHasKitchen()) {
                it.remove();
            }else if (dtoExtras.isHasDishWasher() == dto.isHasDishWasher() && !dto.isHasDishWasher()) {
                it.remove();
            }else if (dtoExtras.isHasChildrenPlayground() == dto.isHasChildrenPlayground() && !dto.isHasChildrenPlayground()) {
                it.remove();
            }else if (dtoExtras.isHasParking() == dto.isHasParking() && !dto.isHasParking()) {
                it.remove();
            }else if (dtoExtras.isHasWifi() == dto.isHasWifi() && !dto.isHasWifi()) {
                it.remove();
            }else if (dtoExtras.isHasWashingMachine() == dto.isHasWashingMachine() && !dto.isHasWashingMachine()) {
                it.remove();
            }else if (dtoExtras.isHasTV() == dto.isHasTV() && !dto.isHasTV()) {
                it.remove();
            }else if (dtoExtras.isHasYard() == dto.isHasYard() && !dto.isHasYard()) {
                it.remove();
            }
        }
        int size = 8;
        PageRequest page = PageRequest.of((int) pageIdx,size);
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), allProperties.size());
        int totalRows = allProperties .size();
        Page<Property> pageToReturn = new PageImpl<Property>(allProperties .subList(start, end), page, totalRows);
        PageDto returnDto = new PageDto();
        returnDto.setProperties(getPagePropertyDtos(pageToReturn.get().collect(Collectors.toList())));
        returnDto.setCurrentPage((int) pageIdx);
        returnDto.setTotalItems(allProperties.size());
        returnDto.setTotalPages(returnDto.getTotalItems()/8);
        return returnDto;
    }
    private List<PagePropertyDto> getPagePropertyDtos(List<Property> propertiesByPrice) {
        List<PagePropertyDto> dto = propertiesByPrice.stream().map(property -> modelMapper.map(property,PagePropertyDto.class)).collect(Collectors.toList());
        for (int i = 0; i < propertiesByPrice.size(); i++) {
            dto.get(i).setAddress(propertiesByPrice.get(i).getAddress());
        }
        return dto;
    }
}
