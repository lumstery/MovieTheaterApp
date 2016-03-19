package com.epam.moovies.service.impl;

import com.epam.moovies.discount.DiscountStrategy;
import com.epam.moovies.model.Seat;
import com.epam.moovies.model.Ticket;
import com.epam.moovies.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DiscountServiceImpl implements DiscountService  {
    public static final int VIP_PLACE_KOEF = 2;

    public Ticket applyDiscounts(Ticket ticket) {

        Long discount = 0L;
        Long realPrice = ticket.getRealPrice();

        DiscountStrategy[] discountStrategies = DiscountStrategy.values();
        for (DiscountStrategy strategy: discountStrategies){
            Long currentDiscount = strategy.getDiscountCalculator().getDiscount(ticket);
            if(currentDiscount>0){
                ticket.setDiscountStrategy(strategy);
                discount = currentDiscount;
                break;
            }

        }
        if((discount>0)&&(discount>realPrice)){
            ticket.setTotalPrice(0L);
        }else{
            ticket.setTotalPrice(realPrice-discount);
        }
        return ticket;
    }

    public  Ticket   evaluateAndSetTicketPrice(Ticket ticket) {
        Long resultprice = getPriceOfTicket(ticket);
        ticket.setRealPrice(resultprice);
        applyDiscounts(ticket);
        return  ticket;
    }

    public Long getPriceOfTicket(Ticket ticket){
        Long resultprice = 0L;
        Long basePrice = ticket.getEvent().getBasePrice();
        List<Seat> bookedSeats = ticket.getBookedSeats();
        for (Seat seat : bookedSeats) {
            if (seat.isVip()) {
                resultprice += basePrice * VIP_PLACE_KOEF;
            } else {
                resultprice += basePrice;
            }
        }
        return resultprice;
    }
}
