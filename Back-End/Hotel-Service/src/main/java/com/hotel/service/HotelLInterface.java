package com.hotel.service;

import com.hotel.dto.HotelDto;
import com.hotel.entity.Hotel;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface HotelLInterface {

    Hotel saveHotel(String date, MultipartFile file);

    HotelDto deleteHotel(String hid);

    List<HotelDto> findAllHotelsRoom();

    ResponseEntity<UrlResource> getImageBuUrl(String name);

    ResponseEntity<?> getPageableData(int pageNumber, int pageSize);

    HotelDto  getSingelRoom(String hid);

    HotelDto updateResource(String hid, MultipartFile file,  String data);

    ResponseEntity<List<HotelDto>> getRoomsByTypes(String type, int pageNumber, int pageSize );
}
