package com.ittalents.airbnb.services;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyCreationDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyResponseDto;
import com.ittalents.airbnb.model.entity.Address;
import com.ittalents.airbnb.model.entity.Photo;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.entity.User;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.exceptions.UnauthorizedException;
import com.ittalents.airbnb.util.SessionManager;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService extends AbstractService{

    private static final String PROPERTY_PHOTOS_PATH = "photos/properties_photos";

    public PropertyCreationDto add(PropertyCreationDto dto, long id){
        if(!getUserById(id).isHost()){
            throw new BadRequestException("User isn't a host!");
        }
        Property p = modelMapper.map(dto,Property.class);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!")));

        long extrasB=0;
        extrasB +=  dto.isHasWifi() ? Math.pow(2,0):0;
        extrasB +=  dto.isHasBalcony() ? Math.pow(2,1):0;
        extrasB +=  dto.isHasAirConditioning() ? Math.pow(2,2):0;
        extrasB +=  dto.isHasWashingMachine() ? Math.pow(2,3):0;
        extrasB +=  dto.isHasDishWasher() ? Math.pow(2,4):0;
        extrasB +=  dto.isHasBabyCrib() ? Math.pow(2,5):0;
        extrasB +=  dto.isHasYard() ? Math.pow(2,6):0;
        extrasB +=  dto.isHasParking() ? Math.pow(2,7):0;
        extrasB +=  dto.isHasKitchen() ? Math.pow(2,8):0;
        extrasB +=  dto.isHasTV() ? Math.pow(2,9):0;
        extrasB +=  dto.isHasChildrenPlayground() ? Math.pow(2,10):0;
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
    public List<GeneralPropertyResponseDto> getUserProperties(long id){
        User u = getUserById(id);
        if(!u.isHost()){
            throw new BadRequestException("The user is not host");
        }
        List<GeneralPropertyResponseDto> responseDto = new ArrayList<>();
        //responseDto = u.getProperties().stream().map(property -> modelMapper.map(property, GeneralPropertyResponseDto.class) ).collect(Collectors.toList());
        for(Property property: u.getProperties()){
            GeneralPropertyResponseDto dto;
            dto =  modelMapper.map(property, GeneralPropertyResponseDto.class);
            putExtras(dto,property.getExtras());
           responseDto.add( modelMapper.map(property, GeneralPropertyResponseDto.class));
        }
        return responseDto;
    }

    public GeneralPropertyResponseDto getPropertyById(long id) {
       // propertyRepository.findById(id);
        Property p = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("Property not found!"));
        GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
        putExtras(dto, p.getExtras());
        return dto;
    }

    public List<GeneralPropertyResponseDto> findAll() {
        List<GeneralPropertyResponseDto> res = new ArrayList<>();
        for(Property p : propertyRepository.findAll()){
            GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
            long extras = p.getExtras();
            putExtras(dto,extras);
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
  public  void putExtras(GeneralPropertyResponseDto dto,long extras){

        System.out.println(extras);
        for (int i = 0; i <= 10 ; i++) {
            int num = (int)Math.pow(2,i);
            if((extras&num)>0){
                switch(i){
                    case 0:dto.setHasWifi(true);break;
                    case 1:dto.setHasBalcony(true);break;
                    case 2:dto.setHasAirConditioning(true);break;
                    case 3:dto.setHasWashingMachine(true);break;
                    case 4:dto.setHasDishWasher(true);break;
                    case 5:dto.setHasBabyCrib(true);break;
                    case 6:dto.setHasYard(true);break;
                    case 7:dto.setHasParking(true);break;
                    case 8:dto.setHasKitchen(true);break;
                    case 9:dto.setHasTV(true);break;
                    case 10:dto.setHasChildrenPlayground(true);break;

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
        propertyRepository.delete(p);
        return dto;
    }
}
