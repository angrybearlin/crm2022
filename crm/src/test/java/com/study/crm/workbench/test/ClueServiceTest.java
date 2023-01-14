package com.study.crm.workbench.test;

import com.study.crm.workbench.domain.Clue;
import com.study.crm.workbench.service.ClueService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-datasource.xml",
        "classpath:applicationContext-mvc.xml"})
public class ClueServiceTest {
    @Autowired
    private ClueService clueService;

    @Test
    public void testUpdateClueById() {
        Clue clue = new Clue();
        clue.setId("251647f4129c4a8bbf7d1203558745d0");
        clue.setAddress("改之后地址");
        clue.setAppellation("31539e7ed8c848fc913e1c2c93d76fd1");
        clue.setCompany("改之后公司");
        clue.setContactSummary("改之后联系纪要");
        clue.setDescription("改之后描述");
        clue.setEmail("ergou@163.com");
        clue.setFullname("改之后名字");
        clue.setJob("改之后工作");
        clue.setMphone("13011111111");
        clue.setNextContactTime("2024-01-01");
        clue.setOwner("06f5fc056eac41558a964f96daa7f27c");
        clue.setSource("12302fd42bd349c1bb768b19600e6b20");
        clue.setState("06e3cbdf10a44eca8511dddfc6896c55");
        clue.setWebsite("http://www.baidu.com");
        int count = clueService.updateClue(clue);
        Assert.assertEquals(count, 1);
        Clue clue1 = clueService.queryClueForDetailById("251647f4129c4a8bbf7d1203558745d0");
        Assert.assertEquals("改之后地址", clue1.getAddress());
    }

}
