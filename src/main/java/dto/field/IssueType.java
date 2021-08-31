package dto.field;

import com.google.gson.annotations.SerializedName;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "issuetype")
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueType {

    public IssueType(String url, int id, String description, String iconUrl, String name, boolean subtask, int avatarId) {
        this.url = url;
        this.id = id;
        this.description = description;
        this.iconUrl = iconUrl;
        this.name = name;
        this.subtask = subtask;
        this.avatarId = avatarId;
    }

    public IssueType() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueType issueType = (IssueType) o;
        return id == issueType.id &&
                subtask == issueType.subtask &&
                avatarId == issueType.avatarId &&
                Objects.equals(url, issueType.url) &&
                Objects.equals(description, issueType.description) &&
                Objects.equals(iconUrl, issueType.iconUrl) &&
                Objects.equals(name, issueType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, id, description, iconUrl, name, subtask, avatarId);
    }

    @SerializedName("url")
    private String url;
    private int id;
    private String description;
    private String iconUrl;
    private String name;
    private boolean subtask;
    private int avatarId;
}
