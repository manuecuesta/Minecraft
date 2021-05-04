/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Paintball;
import com.gmail.cuestamanue7.juego.Equipo;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

/**
 *
 * @author Manuel
 */
public class PartidaListener implements Listener{
    
    private Paintball plugin;
    
    public PartidaListener(Paintball plugin) {
        this.plugin=plugin;
    }
    
    @EventHandler
    public void alSalir(PlayerQuitEvent event) {
        Player jugador = event.getPlayer();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
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
        String comando=event.getMessage().toLowerCase();
        Player jugador = event.getPlayer();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
            List<String> comandos = new ArrayList<String>();
            comandos.add("/paintball leave");
            comandos.add("/lobby");
            comandos.add("/msg");
            comandos.add("/tell");
            for (int i=0; i<comandos.size(); i++) {
                if (comando.startsWith(comandos.get(i))) {
                    return;
                }
            }
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void romperbloques(BlockBreakEvent event) {
        Player jugador = event.getPlayer();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void dropearitems(PlayerDropItemEvent event) {
        Player jugador = event.getPlayer();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void interactuarinventario(InventoryClickEvent event) {
        Player jugador = (Player) event.getWhoClicked();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
            if (event.getInventory().getType().equals(InventoryType.PLAYER) || event.getInventory().getType().equals(InventoryType.CRAFTING) && event.getSlotType() !=null && event.getCurrentItem() != null) {
                if (!event.getCurrentItem().getType().equals(Material.AIR) && !event.getCurrentItem().getType().equals(Material.SNOW_BALL)) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void ponerbloques(BlockPlaceEvent event) {
        Player jugador = event.getPlayer();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void alDañar(EntityDamageEvent event) {
        Entity entidad= event.getEntity();
        if (entidad instanceof Player) {
            Player jugador= (Player) entidad;
            Partida partida = plugin.getPartidaJugador(jugador.getName());
            if (partida!=null) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void niveldecomida(FoodLevelChangeEvent event) {
        Player jugador = (Player) event.getEntity();
        Partida partida  = plugin.getPartidaJugador(jugador.getName());
        if (partida!=null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void chatear(AsyncPlayerChatEvent event) {
        Player jugador=event.getPlayer();
        if (!event.isCancelled()) {
            Partida partida  = plugin.getPartidaJugador(jugador.getName());
            if (partida!=null) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    Partida partidaP = plugin.getPartidaJugador(p.getName());
                    if (partidaP==null) {
                        event.getRecipients().remove(p);
                    } else {
                        if (!partidaP.getNombre().equals(partida.getNombre())) {
                            event.getRecipients().remove(p);
                        }
                    }
                }
            } else {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if (plugin.getPartidaJugador(p.getName())!=null) {
                        event.getRecipients().remove(p);
                    }
                }
            }
        }
    }
    
    
    @EventHandler
    public void impactoBoladeNieve(EntityDamageByEntityEvent event) {
        Entity e=event.getDamager();
        if (e instanceof Projectile && e.getType().equals(EntityType.SNOWBALL)) {
            Projectile proyectil= (Projectile) e;
            ProjectileSource shooter =proyectil.getShooter();
            Entity dañado = event.getEntity();
            if (dañado instanceof Player && shooter instanceof Player) {
                Player jugadordañado = (Player) dañado;
                Player jugadorasesino = (Player) shooter;
                
                Partida partidadañado = plugin.getPartidaJugador(dañado.getName());
                Partida partidasesino = plugin.getPartidaJugador(jugadorasesino.getName());
                
                if (partidadañado!=null && partidasesino!=null && partidadañado.getNombre().equals(partidasesino.getNombre())) {
                    if (partidadañado.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                        event.setCancelled(true);
                        
                        Jugador jdañado = partidadañado.getjugador(jugadordañado.getName());
                        Jugador jasesino = partidasesino.getjugador(jugadorasesino.getName());
                        
                        Equipo equipodañado = partidadañado.getequipojugador(jdañado.getjugador().getName());
                        Equipo equipoasesino = partidadañado.getequipojugador(jasesino.getjugador().getName());
                        
                        
                        if (!equipodañado.gettipo().equals(equipoasesino.gettipo())) {
                            
                            if (!jdañado.isAsesinadorecientemente()) {
                                jdañado.aumentarmuertes();
                                jugadordañado.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHas sido asesinado por: "+jugadorasesino.getName()));
                                jugadordañado.playSound(jdañado.getjugador().getLocation(), Sound.ANVIL_LAND, 10, 1);
                                jdañado.setAsesinadorecientemente(true);
                                jugadordañado.teleport(equipodañado.getSpawn());
                                PartidaManager.setBolasDeNieve(jugadordañado);
                                                                
                                jasesino.aumentarasesinatos();
                                jugadorasesino.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHas asesinado a: "+jugadordañado.getName()));
                                jugadorasesino.playSound(jasesino.getjugador().getLocation(), Sound.LEVEL_UP, 10, 1);
                                jugadorasesino.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 8));
                                        
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        jdañado.setAsesinadorecientemente(false);
                                    }
                                }, 60L);
                                
                            }
                            
                        }
                    }
                }
            }
        }
    }
    
}
