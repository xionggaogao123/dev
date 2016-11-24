package com.db.pet;

import com.pojo.pet.PetTypeEntry;
import org.junit.Test;

public class petDaoTest {

    PetTypeDao petTypeDao=new PetTypeDao();
	
	@Test
	public void addPetTypeEntry()
	{
        PetTypeEntry petTypeEntry =new PetTypeEntry(
                "精灵猴兄妹",
                "monkey",
                "我们是聪明机灵、活泼好动的银河系宠物，来陪伴小主人开心过暑假啦！",
                0,
                1,
                "/img/pet/monkey.png",
                "/img/pet/monkey_min.png",
                "/img/pet/monkey_max.png",
                "/img/pet/monkey_middle.png"

        );
        petTypeDao.addPetTypeEntry(petTypeEntry);
	}

}
