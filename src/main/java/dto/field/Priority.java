package dto.field;

import com.google.gson.annotations.SerializedName;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "priority")
@XmlAccessorType(XmlAccessType.FIELD)
public class Priority {
    public Priority(String url, String iconUrl, String name, int id) {
        this.url = url;
        this.iconUrl = iconUrl;
        this.name = name;
        this.id = id;
    }

    public Priority() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority = (Priority) o;
        return id == priority.id && Objects.equals(url, priority.url) &&
                Objects.equals(iconUrl, priority.iconUrl) &&
                Objects.equals(name, priority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, iconUrl, name, id);
    }

    @SerializedName("self")
    private String url;
    private String iconUrl;
    private String name;
    private int id;
}

