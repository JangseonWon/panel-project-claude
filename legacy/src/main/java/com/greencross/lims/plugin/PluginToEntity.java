package com.greencross.lims.plugin;

import com.greencross.lims.entity.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PluginToEntity {
	private final SheetDAO sheetRepo;
	private final PluginDAO pluginRepo;

	public PluginToEntity(SheetDAO sheetRepo, PluginDAO pluginRepo) {
		this.sheetRepo = sheetRepo;
		this.pluginRepo = pluginRepo;
	}

	public Plugin<?> parse(com.gcgenome.lims.dto.Plugin dto) {
		// Sheet parent = sheetRepo.findById(dto.getSheet()).orElseThrow(()->new RuntimeException("Can't find Sheet:" + dto.getSheet()));
		Plugin<?> entity = pluginRepo.find(UUID.fromString(dto.id())).orElseGet(()->{
			switch(dto.type()) {
				case BATCH: return new PluginBatch().id(UUID.fromString(dto.id())).icon(dto.icon()).description(dto.description()).order(dto.order());
				case UNIT: return new PluginUnit().id(UUID.fromString(dto.id())).icon(dto.icon()).description(dto.description()).order(dto.order());
				case HOOK: return new Hook().id(UUID.fromString(dto.id()));
				default: throw new RuntimeException("Unsupported Plugin type:" + dto.type());
			}
		});
		Set<Sheet> sheets = Arrays.stream(dto.sheets()).map(UUID::fromString).map(sheetRepo::find).map(Optional::get).collect(Collectors.toSet());
		entity.sheets(sheets)
		.name(dto.name())
		.slot(dto.slot())
		.exec(dto.exec())
		.color(dto.color())
		.colorBg(dto.colorBg());
		if(entity instanceof PluginBatch) {
			PluginBatch cast = (PluginBatch)entity;
			cast.icon(dto.icon()).description(dto.description()).order(dto.order());
		} else if(entity instanceof PluginUnit) {
			PluginUnit cast = (PluginUnit)entity;
			cast.icon(dto.icon()).description(dto.description()).order(dto.order());
		} else if(entity instanceof Hook) {
			//Hook cast = (Hook)entity;
		}
		return entity;
	}
}
