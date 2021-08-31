package dto.field;

import com.google.gson.annotations.SerializedName;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "fields")
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {
    public Field(Priority priority, Reporter reporter, IssueType issueType, String createdDate, String description) {
        this.priority = priority;
        this.reporter = reporter;
        this.issuetype = issueType;
        this.createdDate = createdDate;
        this.description = description;
    }

    public Field() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(priority, field.priority) &&
                Objects.equals(reporter, field.reporter) &&
                Objects.equals(issuetype, field.issuetype) &&
                Objects.equals(createdDate, field.createdDate) &&
                Objects.equals(description, field.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, reporter, issuetype, createdDate, description);
    }

    private Priority priority;
    private Reporter reporter;
    private IssueType issuetype;
    @SerializedName("created")
    private String createdDate;
    private String description;
}
