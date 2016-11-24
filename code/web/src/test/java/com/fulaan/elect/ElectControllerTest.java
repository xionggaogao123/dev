package com.fulaan.elect;

import com.fulaan.elect.controller.ElectController;
import com.fulaan.elect.dto.ElectDTO;
import com.pojo.elect.ElectEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Created by qinbo on 15/3/9.
 */
public class ElectControllerTest {

    @Test
    public void addElectTest()
    {
        ElectController controller = new ElectController();

        ElectDTO electDTO = new ElectDTO();

        electDTO.setName("选举二");




    }
}
