package core.android.xuele.net.crhlibcore.xml;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louweijun on 2018-05-15.
 * xml 节点信息
 */
public class XmlTag {


    private String tagName;
    private String value;

    private List<XmlTag> tags = new ArrayList<>();


    public List<XmlTag> getTags() {
        return tags;
    }

    public XmlTag(String tagName) {
        this.tagName = tagName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addTag(XmlTag xmlTag) {
        if (!isTagEnd) {
            tags.add(xmlTag);
        } else {
            throw new RuntimeException("xml resolve add tag pair after finish");
        }
    }



    public String getTagName() {
        return tagName;
    }

    public String getValue() {
        return value;
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
    public XmlTag getLastXmlTag() {
        if (tags.size() == 0) return null;

        return tags.get(tags.size() - 1);
    }

    @Nullable
    public XmlTag getFisrtXmlTag(String name) {
        for (XmlTag tag : tags) {
            if (name.equals(tag.getTagName())) {
                return tag;
            }
        }
        return null;
    }

    public boolean isTagEnd() {
        return isTagEnd;
    }

    private boolean isTagEnd = false;

    public void end() {
        isTagEnd = true;
    }

}
