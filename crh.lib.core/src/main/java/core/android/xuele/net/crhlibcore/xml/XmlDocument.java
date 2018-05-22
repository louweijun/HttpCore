package core.android.xuele.net.crhlibcore.xml;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louweijun on 2018-05-15.
 * xml 文档信息
 */
public class XmlDocument {

    private List<XmlTag> tags = new ArrayList<>();

    private String name;

    public XmlDocument(String name) {
        this.name = name;
    }

    public void addTag(XmlTag xmlTag) {
        if (!isFinishAll) {
            tags.add(xmlTag);
        } else {
            throw new RuntimeException("xml resolve add tag pair after finish");
        }
    }

    public List<XmlTag> getXmlTag(String name) {
        List<XmlTag> dstTags = new ArrayList<>();
        for (XmlTag tag : tags) {
            if (name.equals(tag.getTagName())) {
                dstTags.add(tag);
            }
        }
        return dstTags;
    }


    @Nullable
    public XmlTag getFirstXmlTag(String name) {
        for (XmlTag tag : tags) {
            if (name.equals(tag.getTagName())) {
                return tag;
            }
        }
        return null;
    }


    @Nullable
    public XmlTag getLastXmlTag() {
        if (tags.size() == 0) return null;

        return tags.get(tags.size() - 1);
    }


    private boolean isFinishAll = false;

    public void finish() {
        isFinishAll = true;
    }
}
