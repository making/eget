package am.ik.eget.crawler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import am.ik.eget.crawler.LinkNode.LinkType;

public class LinkTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test1() {
        LinkNode link = new LinkNode(
                "http://www.dmm.co.jp/monthly/s1/-/list/=/sort=date/", "s1");
        assertEquals(1, link.getPage());
        assertEquals(LinkType.LIST, link.getType());
        assertEquals("", link.getChannel());
    }

    @Test
    public void test2() {
        LinkNode link = new LinkNode(
                "http://www.dmm.co.jp/monthly/s1/-/list/=/sort=date/page=71/",
                "s1");
        assertEquals(71, link.getPage());
        assertEquals(LinkType.LIST, link.getType());
    }

    @Test
    public void test3() {
        LinkNode link = new LinkNode(
                "http://www.dmm.co.jp/monthly/s1/-/detail/=/cid=soe00504/",
                "s1");
        assertEquals(0, link.getPage());
        assertEquals(LinkType.DETAIL, link.getType());
    }

    @Test
    public void test4() {
        LinkNode link = new LinkNode("http://www.dmm.co.jp/monthly/s1/", "s1");
        assertEquals(0, link.getPage());
        assertEquals(LinkType.UNKNOWN, link.getType());
    }
}
