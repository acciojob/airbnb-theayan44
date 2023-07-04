package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {
    HashMap<String, Hotel> hotelDb = new HashMap<>(); // HashMap<HotelName, HotelObject>
    HashMap<Integer, User> userDb = new HashMap<>(); // HashMap<UserAadhaarNumber, UserObject>
    HashMap<String, Booking> bookingDb = new HashMap<>(); // HashMap<BookingId, BookingObject>


    public String addHotel(Hotel hotel) {
        if(hotel == null || hotel.getHotelName() == null || hotel.getHotelName().length() == 0)
            return "FAILURE";
        if(hotelDb.containsKey(hotel.getHotelName()))
            return "FAILURE";

        hotelDb.put(hotel.getHotelName(), hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userDb.put(user.getaadharCardNo(), user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int maxFacilities = 0;
        String ansHotelName = "";
        for(String currHotel : hotelDb.keySet()){
            int currFacilities = hotelDb.get(currHotel).getFacilities().size();
            if(currFacilities > maxFacilities){
                maxFacilities = currFacilities;
                ansHotelName = currHotel;
            }else if(currFacilities == maxFacilities){
                if(currHotel.compareTo(ansHotelName) < 0)
                    ansHotelName = currHotel;
            }
        }
        return ansHotelName;
    }

    public int bookARoom(Booking booking) {
        int bookingPersonAadhaar = booking.getBookingAadharCard();
        String bookingHotelName = booking.getHotelName();
//        if(!hotelDb.containsKey(bookingHotelName) || !userDb.containsKey(bookingPersonAadhaar))
//            return -1;
        if(hotelDb.get(bookingHotelName).getAvailableRooms() < booking.getNoOfRooms())
            return -1;

        int amountPaid = booking.getNoOfRooms() * hotelDb.get(bookingHotelName).getPricePerNight();
        booking.setAmountToBePaid(amountPaid);

        bookingDb.put(booking.getBookingId(), booking);
        return booking.getAmountToBePaid();
    }

    public int getBookings(Integer aadharCard) {
        int count = 0;
        for(String currBooking : bookingDb.keySet()){
            if(bookingDb.get(currBooking).getBookingAadharCard() == aadharCard)
                count++;
        }
        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        if(!hotelDb.containsKey(hotelName))
            return null;

        List<Facility> currFacilitiesList = hotelDb.get(hotelName).getFacilities();
        for(Facility newFacility : newFacilities){
            if(!currFacilitiesList.contains(newFacility))
                currFacilitiesList.add(newFacility);
        }
        hotelDb.get(hotelName).setFacilities(currFacilitiesList);
        return hotelDb.get(hotelName);
    }
}
