package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;


import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.User;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by coleman on 12/14/17.
 */

public class UserUnitTest {

    @Test
    public void isHandleNull() throws Exception {
        //should return new empty user
        assertEquals(new User(), User.parseSnapshot(null));
    }
}
