package com.fantasticsource.setbonus.client;

import com.fantasticsource.mctools.items.ItemFilter;
import com.fantasticsource.setbonus.common.bonusrequirements.ABonusRequirement;
import com.fantasticsource.setbonus.common.bonusrequirements.setrequirement.Set;
import com.fantasticsource.setbonus.common.bonusrequirements.setrequirement.SetRequirement;
import com.fantasticsource.setbonus.config.SetBonusConfig;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.*;

public class TooltipRenderer
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltips(ItemTooltipEvent event)
    {
        if (!SetBonusConfig.clientSettings.enableTooltips) return;

        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;

        List<String> tooltip = event.getToolTip();

        boolean edited = false;
        for (Set set : ClientData.sets.values())
        {
            for (ItemFilter filter : set.involvedEquips.values())
            {
                if (filter.matches(event.getItemStack()))
                {
                    if (!edited)
                    {
                        edited = true;
                        tooltip.add("");
//                        tooltip.add("" + LIGHT_PURPLE + UNDERLINE + I18n.translateToLocalFormatted(SetBonus.MODID + ".tooltip.pressDetailKey"));
//                        tooltip.add("");
                    }
                    int count = set.getNumberEquipped(player);
                    int max = set.getMaxNumber();
                    String color = "" + (count == 0 ? RED : count == max ? GREEN : YELLOW);
                    tooltip.add(color + BOLD + "=== " + set.name + " (" + count + "/" + max + ") ===");
                    for (ClientBonus bonus : ClientData.bonuses.values())
                    {
                        int req = 0;
                        boolean otherReqs = false;

                        for (ABonusRequirement requirement : bonus.bonusRequirements)
                        {
                            if (requirement instanceof SetRequirement)
                            {
                                SetRequirement setRequirement = ((SetRequirement) requirement);
                                if (setRequirement.set.id.equals(set.id))
                                {
                                    req = Tools.max(req, setRequirement.num);
                                }
                                else otherReqs = true;
                            }
                            else otherReqs = true;
                        }

                        if (req > 0)
                        {
                            ClientBonus.BonusInstance bonusInstance = bonus.getBonusInstance(player);

                            color = "";
                            int active = set.getNumberEquipped(player);

                            if (bonusInstance.active) color += GREEN; //All requirements met
                            else
                            {
                                if (active >= req) color += DARK_PURPLE; //Set requirements are met, but non-set requirements are not met
                                else if (active == 0) color += RED; //No set requirements met
                                else color += YELLOW; //Some set requirements met
                            }

                            tooltip.add(color + " (" + active + "/" + req + ")" + (otherReqs ? "*" : "") + " " + bonus.name);
                        }
                    }
                    tooltip.add("");
                }
            }
        }
    }
}
