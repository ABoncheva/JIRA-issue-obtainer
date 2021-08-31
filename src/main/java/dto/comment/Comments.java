package dto.comment;

import com.google.gson.annotations.SerializedName;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashSet;

@XmlRootElement(name = "comments")
@XmlAccessorType(XmlAccessType.FIELD)
public class Comments {

    public Comments(int totalCount, Collection<Comment> comments) {
        this.totalCount = totalCount;
        this.comments = comments;
    }

    public Comments() {
        comments = new HashSet<>();
    }

    @SerializedName("total")
    private int totalCount;
    @XmlElement(name = "comment")
    private Collection<Comment> comments;
}
