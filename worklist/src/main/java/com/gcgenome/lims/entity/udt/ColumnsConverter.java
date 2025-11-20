package com.gcgenome.lims.entity.udt;

import com.gcgenome.lims.dto.Sheet;
import org.hibernate.HibernateException;

import java.util.Objects;

public final class ColumnsConverter extends JsonTypeConverter {
	@Override
	public Class<Sheet.ColumnDefinition[]> returnedClass() {
		return Sheet.ColumnDefinition[].class;
	}
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if(x == null && y == null) return true;
		else if(x == null || y == null) return false;
		else if(x instanceof Sheet.ColumnDefinition[] && y instanceof Sheet.ColumnDefinition[]) {
			try {
				String x1 = OM.writeValueAsString(x);
				String x2 = OM.writeValueAsString(y);
				return Objects.equals(x1, x2);
			} catch (Exception e) {
				return false;
			}
		} else return false;
	}
}