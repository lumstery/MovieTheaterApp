package com.epam.movies.discount.calculator.impl;

import com.epam.movies.discount.calculator.DiscountCalculator;
import com.epam.movies.model.Seat;
import com.epam.movies.model.Ticket;
import com.epam.movies.service.impl.DiscountServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Component
public class BirthdayStrategyDiscountCalulator implements DiscountCalculator {

    public Long getDiscount(Ticket ticket) {
        Long discount = 0L;
        if(isUserBirthdayToday(ticket.getCustomer().getBirthDate())){
            Long priceOfTicket = getPriceOfTicket(ticket);
            discount = Double.doubleToLongBits(priceOfTicket * 0.05);
        }
        return discount;
    }



    private boolean isUserBirthdayToday(Date birthDate){
        Calendar birthDateCalendar = Calendar.getInstance();
        birthDateCalendar.setTime(birthDate);
        int birthDayDay = birthDateCalendar.get(Calendar.DAY_OF_MONTH);
        int birthDounthOfYear = birthDateCalendar.get(Calendar.MONTH);
        Calendar todayCalendar = Calendar.getInstance();
        birthDateCalendar.setTime(birthDate);
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
        int todayMounth = todayCalendar.get(Calendar.MONTH);
        return  (birthDayDay==todayDay)&&(birthDounthOfYear==todayMounth);
    }

    public Long getPriceOfTicket(Ticket ticket){
        Long resultprice = 0L;
        Long basePrice = ticket.getEvent().getBasePrice();
        List<Seat> bookedSeats = ticket.getBookedSeats();
        for (Seat seat : bookedSeats) {
            if (seat.isVip()) {
                resultprice += basePrice * DiscountServiceImpl.VIP_PLACE_KOEF;
            } else {
                resultprice += basePrice;
            }
        }
        return resultprice;
    }

}