package com.example.snake;

import com.example.snake.slice.RankAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class RankAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(RankAbilitySlice.class.getName());
    }
}
