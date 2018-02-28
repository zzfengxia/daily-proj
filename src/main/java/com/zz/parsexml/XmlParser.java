package com.zz.parsexml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：解析xml
 */
public class XmlParser {
    private Logger log = LoggerFactory.getLogger(XmlParser.class);

    public void xmlToBeanMapping(InputStream xml, Class Bean) {

    }

    public NodeTree parseXml(String fileName) throws PaiUException {
        return parseXml(new File(fileName));
    }

    public NodeTree parseXml(File file) throws PaiUException {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(file);
            return parseDocument(document);
        } catch (JDOMException e) {
            log.error("", e);
            return null;
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    private NodeTree parseDocument(Document document) throws PaiUException {
        Element element = document.getRootElement();
        List<Element> chidrens = element.getChildren();
        if(chidrens == null || chidrens.size() == 0) {
            throw new PaiUException(ParseException.ERR_NOT_FIND_NODE);
        }
        return null;
    }
}
