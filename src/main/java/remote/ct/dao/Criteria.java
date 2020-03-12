package remote.ct.dao;

import java.io.Serializable;

import remote.ct.model.DataTO;

@FunctionalInterface
public interface Criteria extends Serializable {
	Boolean apply(DataTO data);
}
