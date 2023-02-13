package tech.ada.star.wars.data.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity<T> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private T id;

	public T getId() {
		return this.id;
	}

	public void setId(T id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof BaseEntity<?>)) {
			return false;
		} else {
			BaseEntity<?> other = (BaseEntity<?>) obj;
			return Objects.equals(id, other.id);
		}
	}

	@Override
	public String toString() {
		String simpleName = getClass().getSimpleName();
		return new StringBuilder(simpleName).append("[").append(id).append("]").toString();
	}

}