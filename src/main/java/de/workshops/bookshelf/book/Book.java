package de.workshops.bookshelf.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  private String title;

  @Column(length = 1000)
  private String description;

  private String author;

  private String isbn;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }

    Class<?> oEffectiveClass = o.getClass();
    Class<?> thisEffectiveClass = this.getClass();
    if (o instanceof HibernateProxy hibernateProxy) {
      oEffectiveClass = hibernateProxy.getHibernateLazyInitializer().getPersistentClass();
      thisEffectiveClass = hibernateProxy.getHibernateLazyInitializer().getPersistentClass();
    }
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }

    Book book = (Book) o;

    return getId() != null && Objects.equals(getId(), book.getId());
  }

  @Override
  public final int hashCode() {
    int hashCode = getClass().hashCode();
    if (this instanceof HibernateProxy hibernateProxy) {
      hashCode = hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode();
    }

    return hashCode;
  }
}
