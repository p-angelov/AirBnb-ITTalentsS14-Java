package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.exceptions.BadRequestException;

import com.ittalents.airbnb.model.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
public class FileController extends MasterController {
    @GetMapping("/images/user/{fileName}")
    @SneakyThrows
    public void downloadUserProfile(@PathVariable String fileName, HttpServletResponse response) {
        String folder = "photos" + File.separator + "UserPhotos";
        File file = new File(folder + File.separator + fileName);
        if (!file.exists()) {
            throw new NotFoundException("File doesn't exists!");
        }
        Files.copy(file.toPath(), response.getOutputStream());
    }
    @GetMapping("/images/properties/{fileName}")
    @SneakyThrows
    public void downloadPropertiesProfile(@PathVariable String fileName, HttpServletResponse response) {
        String folder = "photos" + File.separator + "properties_photos";
        File file = new File(folder + File.separator + fileName);
        if (!file.exists()) {
            throw new NotFoundException("File doesn't exists!");
        }
        Files.copy(file.toPath(), response.getOutputStream());
    }

}
