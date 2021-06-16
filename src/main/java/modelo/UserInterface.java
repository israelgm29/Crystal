/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entidades.Operator;

/**
 *
 * @author jhona
 */
public interface UserInterface {
     public Operator findAndLogin(String data, String password) throws Exception;
}
