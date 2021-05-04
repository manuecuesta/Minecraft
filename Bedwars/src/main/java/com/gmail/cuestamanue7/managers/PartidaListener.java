/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Partida;
import com.gmail.cuestamanue7.juego.Tienda;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;

/**
 *
 * @author Manuel
 */
public class PartidaListener implements Listener {

    private Bedwars plugin;

    public PartidaListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Action action = event.getAction();

        Player player = event.getPlayer();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType().equals(Material.FIREBALL)) {
                Fireball f = player.launchProjectile(Fireball.class);
                f.setIsIncendiary(false);
            }
        }
    }

    @EventHandler
    public void Tiendaclick(InventoryClickEvent e
    ) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack item = e.getCurrentItem();

        if (inv.getName().equals("Shop")) {
            e.setCancelled(true);
            if (item.getType() == Material.STAINED_GLASS_PANE) {

            }

        } else if (inv.getName().equals("Upgrades and Traps")) {
            e.setCancelled(true);
            if (item.getType() == Material.IRON_SWORD) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            } else if (item.getType() == Material.IRON_CHESTPLATE) {

            }
        }

    }

    @EventHandler
    public void colocarbloque(BlockPlaceEvent e) {
        Partida p = plugin.getPartidaJugador(e.getPlayer().getName());
        if (p != null) {
            p.addBloques(e.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    public void destruirbloque(BlockBreakEvent e) {
        Partida p = plugin.getPartidaJugador(e.getPlayer().getName());
        if (p != null) {
            ArrayList<Location> bloques = p.getBloques();
            for (int i = 0; i < bloques.size(); i++) {
                if (bloques.get(i).equals(e.getBlock().getLocation())) {
                    bloques.remove(i);
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void entidades(EntityDamageEvent e
    ) {
        if (e.getEntity() instanceof Villager) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void tienda(PlayerInteractEntityEvent e
    ) {
        if (e.getRightClicked() instanceof Villager) {
            Tienda t = new Tienda();
            e.setCancelled(true);
            e.getPlayer().openInventory(t.abrirmejoras());
        }

    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e
    ) {
        if (e.getEntity() instanceof Villager || e.getEntity() instanceof org.bukkit.entity.ArmorStand) {
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void daño(EntityDamageEvent e
    ) {
        if (e.getEntity() instanceof Player) {
            if (plugin.getPartidaJugador(e.getEntity().getName()) == null || !plugin.getPartidaJugador(e.getEntity().getName()).getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void manipulate(PlayerArmorStandManipulateEvent e
    ) {
        if (!e.getRightClicked().isVisible()) {
            e.setCancelled(true);
        }
    }

    private Location getTwinLocation(Bed b) {
        if (b.isHeadOfBed()) {
            return (((Block) b).getRelative(b.getFacing())).getLocation();
        } else {
            return (((Block) b).getRelative(b.getFacing().getOppositeFace())).getLocation();
        }
    }
/*
    @EventHandler
    public void destruircama(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.BED_BLOCK)) {
            Player jugador = e.getPlayer();
            Partida partida = plugin.getPartidaJugador(jugador.getName());
            if (partida != null) {
                for (int i = 0; i < partida.getequipos().size(); i++) {
                    Bed bed = null;
                    Block b = partida.getequipos().get(i).getCama().getBlock();
                    if (b.getType().equals(Material.BED_BLOCK)) {
                        bed = (Bed) b;
                        bed.getFacing().getOppositeFace();

                        if ((int) partida.getequipos().get(i).getCama().getX() == e.getBlock().getX() && (int) partida.getequipos().get(i).getCama().getY() == e.getBlock().getY() && (int) partida.getequipos().get(i).getCama().getZ() == e.getBlock().getZ() && partida.getequipos().get(i).getCama().getWorld().equals(e.getBlock().getWorld())) {
                            if (partida.getequipojugador(jugador.getName()).equals(partida.getequipos().get(i))) {
                                e.setCancelled(true);
                            } else {
                                partida.getequipos().get(i).setCama1(false);
                                e.getBlock().setType(Material.AIR);
                                for (int j = 0; j < partida.getJugadores().size(); j++) {
                                    partida.getJugadores().get(j).getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa cama del equipo: " + partida.getequipos().get(i).gettipo() + " ha sido destruida"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
*/
    @EventHandler
    public void morir(PlayerDeathEvent e) {
        Partida partida = plugin.getPartidaJugador(e.getEntity().getPlayer().getName());
        if (partida != null) {
            if (!partida.getequipojugador(e.getEntity().getPlayer().getName()).isCama1()) {
                PartidaManager.jugadoreliminado(partida, e.getEntity().getPlayer(), plugin);
            }
        }
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        Partida partida = plugin.getPartidaJugador(e.getPlayer().getName());
        if (partida != null) {
            if (partida.getequipojugador(e.getPlayer().getName()) != null) {
                if (partida.getequipojugador(e.getPlayer().getName()).isCama1()) {
                    e.setRespawnLocation(partida.getequipojugador(e.getPlayer().getName()).getCama());
                    PartidaManager.darItemsjugador(e.getPlayer(), partida);
                }
            } else {
                e.setRespawnLocation(partida.getLobby());
            }
        }

    }

    @EventHandler
    public void alSalir(PlayerQuitEvent event) {
        Player jugador = event.getPlayer();
        Partida partida = plugin.getPartidaJugador(jugador.getName());
        if (partida != null) {
            PartidaManager.jugadorSale(partida, jugador, plugin, false, false);
        }
    }

    @EventHandler
    public void alentrar(PlayerJoinEvent event) {
        Player jugador = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        double x = Double.valueOf(config.getString("MainLobby.x"));
        double y = Double.valueOf(config.getString("MainLobby.y"));
        double z = Double.valueOf(config.getString("MainLobby.z"));
        float yaw = Float.valueOf(config.getString("MainLobby.Yaw"));
        float pitch = Float.valueOf(config.getString("MainLobby.Pitch"));
        World world = Bukkit.getWorld(config.getString("MainLobby.world"));
        Location l = new Location(world, x, y, z, yaw, pitch);
        jugador.teleport(l);

    }

    @EventHandler
    public void ejecutarComando(PlayerCommandPreprocessEvent event) {
        String comando = event.getMessage().toLowerCase();
        Player jugador = event.getPlayer();
        Partida partida = plugin.getPartidaJugador(jugador.getName());
        if (partida != null) {
            List<String> comandos = new ArrayList<String>();
            comandos.add("/paintball leave");
            comandos.add("/lobby");
            comandos.add("/msg");
            comandos.add("/tell");
            for (int i = 0; i < comandos.size(); i++) {
                if (comando.startsWith(comandos.get(i))) {
                    return;
                }
            }
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void niveldecomida(FoodLevelChangeEvent event) {
        Player jugador = (Player) event.getEntity();
        Partida partida = plugin.getPartidaJugador(jugador.getName());
        if (partida == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void chatear(AsyncPlayerChatEvent event) {
        Player jugador = event.getPlayer();
        if (!event.isCancelled()) {
            Partida partida = plugin.getPartidaJugador(jugador.getName());
            if (partida != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Partida partidaP = plugin.getPartidaJugador(p.getName());
                    if (partidaP == null) {
                        event.getRecipients().remove(p);
                    } else {
                        if (!partidaP.getNombre().equals(partida.getNombre())) {
                            event.getRecipients().remove(p);
                        }
                    }
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (plugin.getPartidaJugador(p.getName()) != null) {
                        event.getRecipients().remove(p);
                    }
                }
            }
        }
    }
}
