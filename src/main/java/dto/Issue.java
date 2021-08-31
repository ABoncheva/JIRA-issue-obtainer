package dto;

import com.google.gson.annotations.SerializedName;
import dto.comment.Comments;
import dto.field.Field;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "dto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Issue {
    public Issue(String url, String key, Field fields) {
        this.url = url;
        this.key = key;
        this.fields = fields;

    }

    public Issue() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(key, issue.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public String getKey() {
        return key;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    @SerializedName("self")
    private String url;
    private String key;
    private Field fields;
    private Comments comments;
}
