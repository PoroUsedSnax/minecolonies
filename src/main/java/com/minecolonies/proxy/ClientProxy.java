package com.minecolonies.proxy;

import com.minecolonies.MineColonies;
import com.minecolonies.client.gui.WindowBuildTool;
import com.minecolonies.client.gui.WindowCitizen;
import com.minecolonies.client.render.EmptyTileEntitySpecialRenderer;
import com.minecolonies.client.render.RenderBipedCitizen;
import com.minecolonies.client.render.RenderFishHook;
import com.minecolonies.colony.CitizenData;
import com.minecolonies.configuration.Configurations;
import com.minecolonies.entity.EntityCitizen;
import com.minecolonies.entity.EntityFishHook;
import com.minecolonies.event.ClientEventHandler;
import com.minecolonies.network.messages.OpenInventoryMessage;
import com.minecolonies.tileentities.TileEntityColonyBuilding;
import com.schematica.client.events.TickHandler;
import com.schematica.client.renderer.RendererSchematicGlobal;
import com.schematica.world.SchematicWorld;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    private RendererSchematicGlobal rendererSchematicGlobal;
    private SchematicWorld          schematicWorld          = null;

    @Override
    public boolean isClient()
    {
        return true;
    }

    @Override
    public void registerKeyBindings()
    {
//        for(KeyBinding keyBinding : KeyInputHandler.KEY_BINDINGS)
//        {
//            ClientRegistry.registerKeyBinding(keyBinding);
//        }
    }

    @Override
    public void registerEvents()
    {
        super.registerEvents();

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        //Schematica
        FMLCommonHandler.instance().bus().register(new TickHandler());
        this.rendererSchematicGlobal = new RendererSchematicGlobal();
        MinecraftForge.EVENT_BUS.register(this.rendererSchematicGlobal);
    }

    @Override
    public void registerEntityRendering()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityCitizen.class, new RenderBipedCitizen());
        RenderingRegistry.registerEntityRenderingHandler(EntityFishHook.class, new RenderFishHook());

    }

    @Override
    public void registerTileEntityRendering()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColonyBuilding.class, new EmptyTileEntitySpecialRenderer());
    }

    @Override
    public void showCitizenWindow(CitizenData.View citizen)
    {
        WindowCitizen window = new WindowCitizen(citizen);
        window.open();
    }

    @Override
    public void openBuildToolWindow(int x, int y, int z)
    {
        WindowBuildTool window = new WindowBuildTool(x, y, z);
        window.open();
    }

    //Schematica
    @Override
    public void setActiveSchematic(SchematicWorld world)
    {
        this.schematicWorld = world;
    }

    @Override
    public SchematicWorld getActiveSchematic()
    {
        return this.schematicWorld;
    }
}