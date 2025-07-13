package com.hotel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.HotelDto;
import com.hotel.entity.Hotel;
import com.hotel.exception.ImageNotFoundException;
import com.hotel.exception.ResourceNotFoundExcp;
import com.hotel.repo.HotelRepo;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Service
public class HotelImpel implements HotelLInterface {

    final String basePath = "http://localhost:8080/hotel/";
    @Autowired
    private HotelRepo hotelRepo;

    @Override
    @Transactional
    @CachePut(value = "room", key="#result.hid()")
    public Hotel saveHotel(String date, MultipartFile file) {

        String imageName = file.getOriginalFilename()+"_"+System.currentTimeMillis();
        ObjectMapper objectMapper = new ObjectMapper();
        Hotel hotel;
        try {
            hotel =  objectMapper.readValue(date, Hotel.class);
        } catch (JsonProcessingException e) {throw new RuntimeException(e);}

        //save image in folder
        Path userDirectory = Paths.get(System.getProperty("user.dir"), "Images");

        if(!Files.exists(userDirectory)){
            try {
                Files.createDirectories(userDirectory);
            } catch (IOException e) {throw new RuntimeException(e);}}

        try {
            Path path = userDirectory.resolve(imageName);
            Files.copy(file.getInputStream(), path,  StandardCopyOption.REPLACE_EXISTING);
            hotel.setHid(UUID.randomUUID().toString().substring(0,7));
            hotel.setImageurl(basePath+"Images/"+imageName);
            hotel.setImageid(imageName);
        } catch (IOException e) {throw new RuntimeException(e); }

        System.out.println(hotel);
        hotelRepo.save(hotel);
        return hotel;

    }



    @Override
    @Transactional
    @CacheEvict(value = "room", key = "#hid")
    public HotelDto deleteHotel(String hid) {
        Hotel hotel = hotelRepo.findById(hid).orElseThrow(() -> new ResourceNotFoundExcp());

        //delete image as well
        Hotel hotel1 = hotelRepo.findById(hid).get();
        String hotelID = hotel1.getImageid();

        Path p = Paths.get(System.getProperty("user.dir"), "Images");
        Path path = p.resolve(hotelID);

        try {
            Files.delete(path);
        } catch (IOException e) {throw new RuntimeException(e);}

        HotelDto hotelDto = new HotelDto(hotel.getHid(), hotel.getRoomtype(), hotel.getImageurl(), hotel.getRoomprice(), hotel.getImageid());

        hotelRepo.delete(hotel);
        return hotelDto;
    }


    @Override
    @Cacheable(value = "rooms")
    public List<HotelDto> findAllHotelsRoom() {
        return hotelRepo.findAll()
                .stream()
                .map(hotel -> new HotelDto(hotel.getHid(), hotel.getRoomtype(), hotel.getImageurl(), hotel.getRoomprice(), hotel.getImageid()))
                .toList();
    }


    @Override
    public ResponseEntity<UrlResource> getImageBuUrl(String imageid) {

        Path dir = Paths.get(System.getProperty("user.dir"), "Images");
        Path path = dir.resolve(imageid);
        //System.out.println(path);
        UrlResource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(resource);
        if(resource.exists()){
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        }else{
           // System.out.println("No  data");
            throw  new ImageNotFoundException("Not Image of given name");
        }


    }

    @Override
    public ResponseEntity<List<HotelDto>> getPageableData(int pageNumber, int pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Hotel> hp = hotelRepo.findAll(p);
        List<Hotel> hotels =  hp.getContent();
        List<HotelDto> hDTO =  hotels.stream()
                .map(hotel -> new HotelDto(hotel.getHid(), hotel.getRoomtype(), hotel.getImageurl(), hotel.getRoomprice(), hotel.getImageid()))
                .toList();
        return  new ResponseEntity<>(hDTO, HttpStatus.ACCEPTED);
    }


    @Override
    @Cacheable(value = "room", key = "#hid")
    public HotelDto getSingelRoom(String hid) {
           Hotel hotel =  hotelRepo.findById(hid).orElseThrow(()->new ResourceNotFoundExcp("No such Room is present here.."));
           HotelDto dto = new HotelDto(hotel.getHid(), hotel.getRoomtype(), hotel.getImageurl(), hotel.getRoomprice(), hotel.getImageid());
            return dto;
        }



    @Override
    @Transactional
    @CachePut(value = "room", key = "#hid")
    public HotelDto updateResource(String hid, MultipartFile file,  String data) {
       ObjectMapper objectMapper = new ObjectMapper();

       Hotel h = null;
         try {
             h   =  objectMapper.readValue(data,  Hotel.class);
         } catch (JsonProcessingException e) { new RuntimeException(e);}

         Hotel hotel = hotelRepo.findById(hid).orElseThrow(()->new ResourceNotFoundExcp("update failed"));
         hotel.setRoomtype(h.getRoomtype());
         hotel.setRoomprice(h.getRoomprice());

            if(!(file == null)) {
            String imageUUID = file.getOriginalFilename() + "_" + System.currentTimeMillis();
            //delete the prev
            Path p = Paths.get(System.getProperty("user.dir"), "Images");
            Path path = p.resolve(hotel.getImageid());
            try {
                Files.delete(path);
                }catch (IOException e) {throw new RuntimeException(e);}

             System.out.println("to delete the path");
             System.out.println(path);

             //now save the new one
             Path pathNew = p.resolve(imageUUID);
             try {
                 Files.copy(file.getInputStream(), pathNew, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {throw new RuntimeException(e);}
                System.out.println("to save the path");
                System.out.println(pathNew);

             hotel.setImageid(imageUUID);
             hotel.setImageurl(basePath + "Images/" + imageUUID);
       }

        System.out.println(hotel);
        hotelRepo.save(hotel);

        HotelDto hotelDto = new HotelDto(hotel.getHid(), hotel.getRoomtype(), hotel.getImageurl(), hotel.getRoomprice(), hotel.getImageid());
        return  hotelDto;
    }

    @Override
    public ResponseEntity<List<HotelDto>> getRoomsByTypes(String type, int pageNumber, int pageSize) {
        Pageable p = PageRequest.of(pageNumber,  pageSize);
        List<Hotel> hotels =  hotelRepo.findByRoomtype(p, type);
        List<HotelDto> hDTO =  hotels.stream()
                .map(hotel -> new HotelDto(hotel.getHid(), hotel.getRoomtype(), hotel.getImageurl(), hotel.getRoomprice(), hotel.getImageid()))
                .toList();
        return new ResponseEntity<List<HotelDto>>(hDTO, HttpStatus.ACCEPTED);
    }


}
