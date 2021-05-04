/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.Equipo;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Generador;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
import com.gmail.cuestamanue7.juego.Tienda;
import static com.gmail.cuestamanue7.managers.PartidaDrops.taskID;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 *
 * @author Manuel
 */
public class PartidaManager {

    int taskID;

    public static void jugadorEntra(Partida partida, Player jugador, Bedwars plugin) {
        Jugador j = new Jugador(jugador);
        partida.agregarJugador(j);
        ArrayList<Jugador> jugadores = partida.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + jugador.getName() + " ha entrado al juego " + partida.getcantidadactualjugadores() + "/" + partida.getcantidadmaximajugadores()));
        }

        jugador.getInventory().clear();
        jugador.getEquipment().clear();
        jugador.getEquipment().setArmorContents(null);
        jugador.updateInventory();

        jugador.setGameMode(GameMode.SURVIVAL);
        jugador.setExp(0);
        jugador.setLevel(0);
        jugador.setFoodLevel(20);
        jugador.setMaxHealth(20);
        jugador.setFlying(false);
        jugador.setAllowFlight(false);
        for (PotionEffect e : jugador.getActivePotionEffects()) {
            jugador.removePotionEffect(e.getType());
        }

        jugador.teleport(partida.getLobby());

        if (partida.getcantidadactualjugadores() >= partida.getcantidadminimajugadores() && partida.getEstadoPartida().equals(EstadoPartida.ESPERANDO)) {
            cooldownIniciarPartida(partida, plugin);
        }

    }

    public static void jugadoreliminado(Partida partida, Player jugador, Bedwars plugin) {
        Jugador j = partida.getjugador(jugador.getName());
        partida.addEliminados(j);
        partida.removerJugador(j.getjugador().getName());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> jugador.spigot().respawn(), 1L);
        jugador.setGameMode(GameMode.SPECTATOR);

        if (partida.getcantidadactualjugadores() <= 1 && partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
            iniciarfasefinalizacion(partida, plugin);
        } else if (partida.jugadoressoloenunequipo() && partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
            iniciarfasefinalizacion(partida, plugin);
        }

    }

    public static void jugadoreliminadotolobby(Partida partida, Bedwars plugin) {
        while (partida.getEliminados().size() != 0) {
            Jugador j = partida.getEliminados().get(0);
            ItemStack[] inventarioGuardado = j.getGuardados().getInventarioguardado();
            ItemStack[] equipamiento = j.getGuardados().getEquipamientoguardado();
            GameMode gamemodeguardado = j.getGuardados().getGamemodeguardado();
            float xpguardada = j.getGuardados().getExperienciaguardada();
            int levelguarada = j.getGuardados().getNivelguardado();
            double vidaguardada = j.getGuardados().getVidaguardada();
            double maxvidaguardad = j.getGuardados().getMaximavidaguardada();

            FileConfiguration config = plugin.getConfig();
            double x = Double.valueOf(config.getString("MainLobby.x"));
            double y = Double.valueOf(config.getString("MainLobby.y"));
            double z = Double.valueOf(config.getString("MainLobby.z"));
            float yaw = Float.valueOf(config.getString("MainLobby.Yaw"));
            float pitch = Float.valueOf(config.getString("MainLobby.Pitch"));
            World world = Bukkit.getWorld(config.getString("MainLobby.world"));
            Location l = new Location(world, x, y, z, yaw, pitch);
            j.getjugador().teleport(l);

            j.getjugador().getInventory().setContents(inventarioGuardado);
            j.getjugador().getEquipment().setArmorContents(equipamiento);
            j.getjugador().setGameMode(gamemodeguardado);
            j.getjugador().setExp(xpguardada);
            j.getjugador().setMaxHealth(maxvidaguardad);
            j.getjugador().setHealth(vidaguardada);
            j.getjugador().updateInventory();
            partida.getEliminados().remove(0);
        }
    }

    public static void jugadorSale(Partida partida, Player jugador, Bedwars plugin, boolean finalizarPartida, boolean cerrandoServer) {
        Jugador j = partida.getjugador(jugador.getName());
        ItemStack[] inventarioGuardado = j.getGuardados().getInventarioguardado();
        ItemStack[] equipamiento = j.getGuardados().getEquipamientoguardado();
        GameMode gamemodeguardado = j.getGuardados().getGamemodeguardado();
        float xpguardada = j.getGuardados().getExperienciaguardada();
        int levelguarada = j.getGuardados().getNivelguardado();
        double vidaguardada = j.getGuardados().getVidaguardada();
        double maxvidaguardad = j.getGuardados().getMaximavidaguardada();

        partida.removerJugador(jugador.getName());

        if (!finalizarPartida) {
            ArrayList<Jugador> jugadores = partida.getJugadores();
            for (int i = 0; i < jugadores.size(); i++) {
                jugadores.get(i).getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + jugador.getName() + " ha salido del juego " + partida.getcantidadactualjugadores() + "/" + partida.getcantidadmaximajugadores()));
            }
        }

        FileConfiguration config = plugin.getConfig();
        double x = Double.valueOf(config.getString("MainLobby.x"));
        double y = Double.valueOf(config.getString("MainLobby.y"));
        double z = Double.valueOf(config.getString("MainLobby.z"));
        float yaw = Float.valueOf(config.getString("MainLobby.Yaw"));
        float pitch = Float.valueOf(config.getString("MainLobby.Pitch"));
        World world = Bukkit.getWorld(config.getString("MainLobby.world"));
        Location l = new Location(world, x, y, z, yaw, pitch);
        jugador.teleport(l);

        jugador.getInventory().setContents(inventarioGuardado);
        jugador.getEquipment().setArmorContents(equipamiento);
        jugador.setGameMode(gamemodeguardado);
        jugador.setExp(xpguardada);
        jugador.setMaxHealth(maxvidaguardad);
        jugador.setHealth(vidaguardada);
        jugador.updateInventory();

        if (!cerrandoServer) {
            if (partida.getcantidadactualjugadores() < partida.getcantidadminimajugadores() && partida.getEstadoPartida().equals(EstadoPartida.COMENZANDO)) {
                partida.setEstado(EstadoPartida.ESPERANDO);
            } else if (partida.getcantidadactualjugadores() <= 1 && partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                //fase finalizacion
                iniciarfasefinalizacion(partida, plugin);
            } else if (partida.jugadoressoloenunequipo() && partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                iniciarfasefinalizacion(partida, plugin);
            }
        }

    }

    private static void cooldownIniciarPartida(Partida partida, Bedwars plugin) {
        partida.setEstado(EstadoPartida.COMENZANDO);
        CooldownManager cooldown = new CooldownManager(plugin);
        cooldown.cooldownComenzarJuego(partida);
    }

    public static void iniciarPartida(Partida partida, Bedwars plugin) {
        partida.setEstado(EstadoPartida.JUGANDO);

        setTeamsAleatorios(partida);
        darItems(partida);
        teletranportarJugadores(partida);
        String prefix = plugin.prefix;
        ArrayList<Jugador> jugadores = partida.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).getjugador().sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&cLa partida ha comenzado"));
        }
        
        camasequipos(partida);
        Tienda.cargartiendas(partida);

        partida.activargeneradores(partida, plugin);

        CooldownManager cooldown = new CooldownManager(plugin);
        cooldown.cooldownJuego(partida);

    }

    public static void iniciarfasefinalizacion(Partida partida, Bedwars plugin) {
        partida.setEstado(EstadoPartida.TERMINANDO);
        Equipo ganador = partida.getGanador();
        ArrayList<Jugador> jugadoreskillord = partida.getJugadoresKills();
        String top1 = "";
        String top2 = "";
        String top3 = "";
        int top1kills = 0;
        int top2kills = 0;
        int top3kills = 0;

        if (jugadoreskillord.size() == 1) {
            top1 = jugadoreskillord.get(0).getjugador().getName();
            top1kills = jugadoreskillord.get(0).getasesinatos();
        } else if (jugadoreskillord.size() == 2) {
            top1 = jugadoreskillord.get(0).getjugador().getName();
            top1kills = jugadoreskillord.get(0).getasesinatos();
            top2 = jugadoreskillord.get(1).getjugador().getName();
            top2kills = jugadoreskillord.get(1).getasesinatos();
        } else if (jugadoreskillord.size() > 2) {
            top1 = jugadoreskillord.get(0).getjugador().getName();
            top1kills = jugadoreskillord.get(0).getasesinatos();
            top2 = jugadoreskillord.get(1).getjugador().getName();
            top2kills = jugadoreskillord.get(1).getasesinatos();
            top3 = jugadoreskillord.get(2).getjugador().getName();
            top3kills = jugadoreskillord.get(2).getasesinatos();
        }

        for (Jugador j : jugadoreskillord) {
            j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&m                                         "));
            if (ganador == null) {
                j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSe ha producido un empate"));
            } else {
                j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c Ha ganado el equipo: " + ganador.gettipo()));
            }
            j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTop Kills:"));
            j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTop 1: " + top1 + " Kills: " + top1kills));
            if (!top2.isEmpty()) {
                j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTop 2: " + top2 + " Kills: " + top2kills));
            } else if (!top3.isEmpty()) {
                j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTop 2: " + top2 + " Kills: " + top2kills));
                j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTop 3: " + top3 + " Kills: " + top3kills));
            }
            j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTus Kills: " + j.getasesinatos()));
            j.getjugador().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&m                                         "));

        }
        
        
        CooldownManager cooldown = new CooldownManager(plugin);
        cooldown.cooldownfinJuego(partida, ganador);
       
    }

    private static void setTeamsAleatorios(Partida partida) {
        ArrayList<Jugador> jugadorescopia = (ArrayList<Jugador>) partida.getJugadores().clone();

        Random r = new Random();
        int num = jugadorescopia.size() / 2;

        for (int i = 0; i < num; i++) {
            int pos = r.nextInt(jugadorescopia.size());
            Jugador jugadorseleccionado = jugadorescopia.get(pos);
            jugadorescopia.remove(pos);

            partida.repartirJugadores();
        }

    }

    private static void darItems(Partida partida) {
        ArrayList<Jugador> jugadores = partida.getJugadores();
        for (Jugador j : jugadores) {
            Player p = j.getjugador();
            Equipo equipo = partida.getequipojugador(p.getName());
            ItemStack item0 = new ItemStack(Material.WOOL, 1, (short) 11);

            p.getInventory().setItem(8, new ItemStack(Material.WOOL, 1, (short) 11));

            ItemStack item8 = new ItemStack(Material.WOOD_SWORD, 1);
            p.getInventory().setItem(0, item8);

            ItemStack item = new ItemStack(Material.LEATHER_HELMET, 1);
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(equipo.getcolor());
            item.setItemMeta(meta);
            p.getInventory().setHelmet(item);

            ItemStack item1 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
            LeatherArmorMeta meta1 = (LeatherArmorMeta) item1.getItemMeta();
            meta1.setColor(equipo.getcolor());
            item1.setItemMeta(meta1);
            p.getInventory().setChestplate(item1);

            ItemStack item2 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
            LeatherArmorMeta meta2 = (LeatherArmorMeta) item2.getItemMeta();
            meta2.setColor(equipo.getcolor());
            item2.setItemMeta(meta2);
            p.getInventory().setLeggings(item2);

            ItemStack item3 = new ItemStack(Material.LEATHER_BOOTS, 1);
            LeatherArmorMeta meta3 = (LeatherArmorMeta) item3.getItemMeta();
            meta3.setColor(equipo.getcolor());
            item3.setItemMeta(meta3);
            p.getInventory().setBoots(item3);
        }
    }

    public static void darItemsjugador(Player j, Partida partida) {
        Player p = j;
        Equipo equipo = partida.getequipojugador(p.getName());
        ItemStack item0 = new ItemStack(Material.WOOL, 1, (short) 11);

        p.getInventory().setItem(8, new ItemStack(Material.WOOL, 1, (short) 11));

        ItemStack item8 = new ItemStack(Material.WOOD_SWORD, 1);
        p.getInventory().setItem(0, item8);

        ItemStack item = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(equipo.getcolor());
        item.setItemMeta(meta);
        p.getInventory().setHelmet(item);

        ItemStack item1 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta meta1 = (LeatherArmorMeta) item1.getItemMeta();
        meta1.setColor(equipo.getcolor());
        item1.setItemMeta(meta1);
        p.getInventory().setChestplate(item1);

        ItemStack item2 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta meta2 = (LeatherArmorMeta) item2.getItemMeta();
        meta2.setColor(equipo.getcolor());
        item2.setItemMeta(meta2);
        p.getInventory().setLeggings(item2);

        ItemStack item3 = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta3 = (LeatherArmorMeta) item3.getItemMeta();
        meta3.setColor(equipo.getcolor());
        item3.setItemMeta(meta3);
        p.getInventory().setBoots(item3);
    }

    private static void teletranportarJugadores(Partida partida) {
        ArrayList<Jugador> jugadores = partida.getJugadores();
        for (Jugador j : jugadores) {
            Player p = j.getjugador();
            Equipo equipo = partida.getequipojugador(p.getName());
            p.teleport(equipo.getSpawn());
        }
    }

    public static void lanzarFuegos(ArrayList<Jugador> jugadores) {
        for (Jugador j : jugadores) {
            Firework fw = (Firework) j.getjugador().getWorld().spawnEntity(j.getjugador().getLocation(), EntityType.FIREWORK);
            FireworkMeta fm = fw.getFireworkMeta();
            Type type = Type.BALL;
            Color c1 = Color.AQUA;
            Color c2 = Color.BLUE;
            Color c3 = Color.GREEN;
            FireworkEffect efecto = FireworkEffect.builder().withColor(c1, c2).withFade(c3).with(type).build();
            fm.addEffect(efecto);
            fm.setPower(2);
            fw.setFireworkMeta(fm);
        }

    }

    public static void finalizarPartida(Partida partida, Equipo ganador, Bedwars plugin, boolean cerrandoserver) throws IOException {
        FileConfiguration config = plugin.getConfig();
        ArrayList<Jugador> jugadores = partida.getJugadores();
        for (Jugador j : jugadores) {
            Equipo equipojugador = partida.getequipojugador(j.getjugador().getName());
            boolean esJugadorGanador = false;
            Player jugador = j.getjugador();
            if (ganador != null) {
                if (ganador.gettipo().equals(equipojugador.gettipo()) && !cerrandoserver) {
                    esJugadorGanador = true;
                }
            }
            if (esJugadorGanador) {
                List<String> comandos = config.getStringList("winners_command_rewards");
                CommandSender consola = Bukkit.getServer().getConsoleSender();
                for (int i = 0; i < comandos.size(); i++) {
                    if (comandos.get(i).startsWith("msg %player% ")) {
                        String mensaje = comandos.get(i).replace("msg %player% ", "");
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje));
                    } else {
                        String comandoAenviar = comandos.get(i).replace("%player%", jugador.getName());
                        Bukkit.dispatchCommand(consola, comandoAenviar);
                    }
                }
            }
            Estadisticas.guardarEstadisticas(partida, plugin, jugador);
            jugadorSale(partida, jugador, plugin, true, false);

        }
        ArrayList<Equipo> equipos = partida.getequipos();
        for (int i = 0; i < equipos.size(); i++) {
            equipos.get(i).setCama1(true);
        }
        
        //Bukkit.getServer().unloadWorld(partida.getNombre(), true);
        //plugin.borrarWorld(partida.getNombre());
        //CrearArena.crearmundo(partida.getNombre());
        //Manager.loadSchem(partida.getNombre(), 10000, 100, 10000, Bukkit.getWorld(partida.getNombre()), plugin);
        
        ArrayList<Location> bloques=partida.getBloques();
        for(int i=0; i<bloques.size(); i++) {
            bloques.get(i).getBlock().setType(Material.AIR);
        }
        
        
        partida.setEstado(EstadoPartida.ESPERANDO);
        
    }

    public static void camasequipos(Partida partida) {
        ArrayList<Equipo> equipos = partida.getequipos();
        for (int i = 0; i < equipos.size(); i++) {

            BlockState bedFoot = equipos.get(i).getCama().getBlock().getState();
            BlockState bedHead = bedFoot.getBlock().getRelative(BlockFace.SOUTH).getState();
            bedFoot.setType(Material.BED_BLOCK);
            bedHead.setType(Material.BED_BLOCK);
            bedFoot.setRawData((byte) 0x0);
            bedHead.setRawData((byte) 0x8);
            bedFoot.update(true, false);
            bedHead.update(true, true);

        }
    }

}
