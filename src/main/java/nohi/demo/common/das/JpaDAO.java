package nohi.demo.common.das;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author NOHI
 * @program: nohi-ds
 * @description:
 * @create 2020-09-12 11:04
 **/
public interface  JpaDAO<E, K extends Serializable> extends JpaRepository<E, K>, JpaSpecificationExecutor<E> {
}
