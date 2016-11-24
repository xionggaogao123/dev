package com.fulaan.pet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:D:\\k6kt_git\\code\\web\\src\\main\\webapp\\WEB-INF/spring-servlet.xml")
public class PetControllerTest {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }
    @Test
    public void addPet() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/addPet.do")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void petBagPage() throws Exception {// ????
        MvcResult mvcResult =mockMvc.perform(get("/pet/petbag.do")).
        //MvcResult mvcResult =mockMvc.perform(get("/pet/petCenter.do")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void getIsPetEgg() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/getIsPetEgg.do")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void selAllPet() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/selAllPet.do")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void selectedPet() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/selectedPet.do")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void selectedStudentPet() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/selectedStudentPet.do").param("studentId","551e33089aed693504b2bdbd")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void hatchPet() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/hatchPet.do").param("id","55233c5c57aacd6d758e4507")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void chooseMyPet() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/chooseMyPet.do").param("id","551e46a69aed42089aa25004")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void updatePetName() throws Exception {
        MvcResult mvcResult =mockMvc.perform(get("/pet/updatePetName.do").param("id","55233c3357aaf90b2da3191f").param("petname","神龙大侠")).
                andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


}
