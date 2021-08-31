package dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "issues")
@XmlAccessorType(XmlAccessType.FIELD)
public class Issues {

    public Issues(Collection<Issue> issues) {
        this.issues = (Set<Issue>) issues;
    }

    public Issues() {
        issues = new HashSet<>();
    }

    public Collection<Issue> getIssues() {
        return issues;
    }

    @XmlElement(name = "dto")
    private Set<Issue> issues;
}
