package jp.fishmans.ossl.channeling;

import net.minecraft.entity.LivingEntity;

public record ChannelingContext(Channeling channeling, LivingEntity source, int time) {
}
