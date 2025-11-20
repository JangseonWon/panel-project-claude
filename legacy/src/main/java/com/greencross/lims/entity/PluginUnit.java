package com.greencross.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("UNIT")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class PluginUnit extends Plugin<PluginUnit> implements Comparable<Plugin<?>> {
	@Column(name="icon", length=4)
	private String icon;
	@Column(name="description", length=255)
	private String description;
	@Column(name="\"order\"", length=3)
	private String order;
	@Override
	public int compareTo(Plugin<?> o) {
		if(o instanceof PluginUnit) {
			PluginUnit cast = (PluginUnit)o;
			return order.compareTo(cast.order());
		} else if(o instanceof PluginBatch) {
			PluginBatch cast = (PluginBatch)o;
			return order.compareTo(cast.order());
		} else return -1;
	}
}
