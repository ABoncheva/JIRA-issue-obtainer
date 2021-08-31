package dto.field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "reporter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Reporter {

    public Reporter(String name) {
        this.name = name;
    }

    public Reporter() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reporter reporter = (Reporter) o;
        return Objects.equals(name, reporter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private String name;
}
