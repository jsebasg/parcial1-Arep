/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.escuelaing.arep.parcial1;

/**
 *
 * @author jgarc
 */
public class WeatherServiceException extends Exception {

    public WeatherServiceException(String city_not_found) {
        super(city_not_found);
    }


    
}
