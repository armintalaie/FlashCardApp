package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class AirlineTest {

    private Airline airline;
    private Flight flight ;
    private Flight flight2 ;

    @BeforeEach
     void runBefore(){
        airline = new Airline("fly nation");
        flight = new Flight("AZK01", 15,"Paris","New York" , 11);
        flight2 = new Flight("AMP21", 11,"London","Berlin" , 3);
    }

    @Test
     void testConstructor() {
        assertEquals(airline.getName(), "fly nation");
    }

    @Test
    void testAddFlight() {
        assertEquals(0,airline.numberOfFlights());
        airline.addFlight(this.flight);
        assertEquals(1,airline.numberOfFlights());

        airline.addFlight(this.flight2);
        assertEquals(2,airline.numberOfFlights());

    }

    @Test
    void testFindFlight() {
        assertNull(airline.findFlight("AZK01"));
        airline.addFlight(flight);
        assertEquals(airline.findFlight("AZK01"),flight);
    }
}

