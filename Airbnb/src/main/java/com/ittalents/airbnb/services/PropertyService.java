package com.ittalents.airbnb.services;

import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.addressDto.FullAddressDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyCreationDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyResponseDto;
import com.ittalents.airbnb.model.dto.userDTOs.UserWithoutPropertiesDto;
import com.ittalents.airbnb.model.entity.Address;
import com.ittalents.airbnb.model.entity.Photo;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.repository.PropertyRepository;
import com.ittalents.airbnb.model.repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService extends AbstractService{



    public PropertyCreationDto add(PropertyCreationDto dto, long id){
        if(!getUserById(id).isHost()){
            throw new BadRequestException("User isn't a host!");
        }
        Property p = modelMapper.map(dto,Property.class);
        p.setHost(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!")));

        //todo bitwise operations with extras

        Address a = new Address();
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

        p.setHost(userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found!")));
        a.setProperty(p);
        propertyRepository.save(p);
        return dto;
    }

    public PropertyResponseDto getPropertyById(long id) {
       // propertyRepository.findById(id);
        Property p = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("Property not found!"));
        PropertyResponseDto dto = modelMapper.map(p, PropertyResponseDto.class);
        return dto;
    }

    public List<GeneralPropertyResponseDto> findAll() {
        List<GeneralPropertyResponseDto> res = new ArrayList<>();
        for(Property p : propertyRepository.findAll()){
            GeneralPropertyResponseDto dto = modelMapper.map(p, GeneralPropertyResponseDto.class);
            res.add(dto);
        }
        return res;
    }

    @SneakyThrows
    public PhotoDto uploadPhoto(long id, MultipartFile file) {
        validatePhoto(file);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String photoName = System.nanoTime() + "." + extension;
        Files.copy(file.getInputStream(), new File("photos/properties_photos" + File.separator + photoName).toPath());
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

        PhotoDto dto = modelMapper.map(file, PhotoDto.class);

        return dto;
    }
}
