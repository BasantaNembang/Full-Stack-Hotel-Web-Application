package com.hotel.controler;


import com.hotel.dto.HotelDto;
import com.hotel.service.CacheInsPection;
import com.hotel.service.HotelImpel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelImpel hotelImpel;

    @Autowired
    private CacheInsPection cacheInsPection;

    //add
    @PostMapping("/add")
    public ResponseEntity<HotelDto> addNewRoom(@RequestPart("file") MultipartFile file, @RequestPart("data") String data) {
          return ResponseEntity.status(HttpStatus.OK).body(hotelImpel.saveHotel(file, data));
    }


    //delete room
    @DeleteMapping("/delete/{hid}")
    public ResponseEntity<HotelDto> deleteRoom(@PathVariable("hid") String hid) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelImpel.deleteHotel(hid));
    }


    //get all hotels
    @GetMapping("/get")
    public List<HotelDto> getAllHotelRooms() {
         return hotelImpel.findAllHotelsRoom();
    }


    //get image by URL
    @GetMapping("/Images/{imageid}")
    public ResponseEntity<UrlResource> sendImage(@PathVariable("imageid") String imageid){
       return hotelImpel.getImageBuUrl(imageid);
    }


    //implementing the Paginations
    @GetMapping("/get-pagination")
    public ResponseEntity<List<HotelDto>> getPagiantionData(@RequestParam("pageNumber") int pageNumber,
                                              @RequestParam("pageSize") int pageSize){
        return hotelImpel.getPageableData(pageNumber, pageSize);
    }

    //get singel hotel
    @GetMapping("/get-room/{hid}")
    public HotelDto getSingelRoom(@PathVariable("hid") String hid){
        return hotelImpel.getSingelRoom(hid);
    }


    //update room
    @PutMapping("/update/{hid}")
    public ResponseEntity<HotelDto> updateResources(@PathVariable("hid") String hid,
                                             @RequestPart(value = "file", required = false) MultipartFile file,
                                             @RequestPart(value = "data",  required = false) String data) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelImpel.updateResource(hid, file, data));
     }


     //get room by type
    @GetMapping("/room/{type}")
    public ResponseEntity<List<HotelDto>> getRoomByType(@PathVariable("type") String type, @RequestParam("pageNumber") int pageNumber,
                                                        @RequestParam("pageSize") int pageSize){
        return hotelImpel.getRoomsByTypes(type, pageNumber, pageSize);
    }

    //for hotels cache-data
    @GetMapping("/cache/{name}")
    public void getCacheInfo(@PathVariable("name") String name){
         cacheInsPection.printCache(name);
    }


}



