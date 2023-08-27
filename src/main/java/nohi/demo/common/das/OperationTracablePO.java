package nohi.demo.common.das;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-05 21:15
 **/
@Getter
@Setter
@MappedSuperclass
public class OperationTracablePO<K extends Serializable>{

    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss", timezone = "GMT+8")
    @Column(name = "CREATED_TS", updatable = false)
    private Date createdTs;

    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss", timezone = "GMT+8")
    @Column(name = "LAST_UPD_TS")
    private Date lastUpdTs;

    @Column(name = "LAST_UPD_BY")
    private String lastUpdBy;

    @PrePersist
    public void beforePersist() {
        Date now = new Date();
        this.createdTs = now;
        this.createdBy = "NONE";
        this.lastUpdTs = now;
        this.lastUpdBy = "NONE";
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdBy = "NONE";
        this.lastUpdTs = new Date();
    }
}
