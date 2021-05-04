/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7;

import com.gmail.cuestamanue7.juego.Equipo;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Generador;
import com.gmail.cuestamanue7.juego.Partida;
import com.gmail.cuestamanue7.juego.TipoGenerador;
import com.gmail.cuestamanue7.managers.CrearArena;
import com.gmail.cuestamanue7.managers.PartidaManager;
import com.gmail.cuestamanue7.worldedit.Manager;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class Comando implements CommandExecutor {

    private Bedwars plugin;

    public Comando(Bedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player jugador = (Player) sender;
        FileConfiguration messages = plugin.getmessages();

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                //paintball create <arena>
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        FileConfiguration config = plugin.getConfig();
                        if (plugin.getPartidas() == null && config.contains("MainLobby")) {
                            Partida partida = new Partida(nombre);
                            CrearArena.crearmundo(jugador, partida);
                            plugin.agregarPartida(partida);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida: " + nombre + " ha sido creada correctamente"));
                        } else {
                            if (plugin.getPartida(nombre) != null) {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida ya existe"));
                            } else {

                                if (!config.contains("MainLobby")) {
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTienes que definir el lobby principal usando /paintball setlobby"));
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRecuerda usar /paintbal help para modificar la partida"));
                                    return true;

                                }
                                Partida partida = new Partida(nombre);
                                plugin.agregarPartida(partida);
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida: " + nombre + " ha sido creada correctamente"));

                            }

                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball create <arena>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                //paintball delete <arena>
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        if (plugin.getPartida(nombre) != null) {
                            plugin.removerPartida(nombre);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida con nombre: " + nombre + " ha sido eliminada"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida que intentas eliminar no existe"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball create <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }

                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                //paintball reload
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    plugin.reloadConfig();
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cConfig recargada correctamente"));
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setlobby")) {
                //paintball setlobby <nombre>
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.setLobby(jugador.getLocation().clone());
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLobby configurada correctamente para la arena con nombre: " + nombre));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes ponerle un lobby"));
                        }

                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /bedwars setlobby <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setlobby")) {
                //paintball setlobby <nombre>
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.setLobby(jugador.getLocation().clone());
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLobby configurada correctamente para la arena con nombre: " + nombre));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes ponerle un lobby"));
                        }

                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /bedwars setlobby <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setmode")) {
                //paintball setlobby <nombre>
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.setModo(args[2]);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cModo de partida configurado correctamente: " + nombre));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes asginarle un modo"));
                        }

                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /bedwars setmode <arena> <mode>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setspawn")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            String equipo = args[2];
                            ArrayList<Equipo> equipos = p.getequipos();
                            for (int i = 0; i < equipos.size(); i++) {
                                if (equipo.equals(equipos.get(i).gettipo())) {
                                    equipos.get(i).setSpawn(jugador.getLocation().clone());
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSpawn para el equipo: " + equipos.get(i).gettipo() + " definido correctamente"));
                                }
                            }

                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes ponerle un spawn"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setspawn <arena> <equipo>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }

            } else if (args[0].equalsIgnoreCase("setbed")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            String equipo = args[2];
                            ArrayList<Equipo> equipos = p.getequipos();
                            for (int i = 0; i < equipos.size(); i++) {
                                if (equipo.equals(equipos.get(i).gettipo())) {
                                    p.getequipos().get(i).setCama(jugador.getLocation().clone());
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCama para el equipo: " + equipos.get(i).gettipo() + " definida correctamente"));
                                }
                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes ponerle una cama"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setbed <arena> <equipo>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }

            } else if (args[0].equalsIgnoreCase("team")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            ArrayList<Equipo> equipos = p.getequipos();
                            p.addequipo(new Equipo(args[2]));
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEquipo con nombre: " + args[2] + " configurado correctamente"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes modificar el nombre del equipo"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setnameteam1 <arena> <nombreEquipo>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setmaxplayers")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            if (Integer.parseInt(args[2]) <= 1) {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLos jugadores maximos de la partida deben ser minimo 2"));
                                return true;
                            } else {
                                p.setcantidadmaxjugadores(Integer.parseInt(args[2]));
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJugadores maximos configurado correctamente"));

                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes modificar sus jugadores maximos"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setmaxplayer <arena> <maxplayers>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setminplayers")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            if (Integer.parseInt(args[2]) <= 1) {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLos jugadores minimos de la partida deben ser minimo 2"));
                                return true;
                            } else if (Integer.parseInt(args[2]) > p.getcantidadmaximajugadores()) {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLos jugadores minimos debe ser un numero menor que los jugadores maximos"));
                                return true;
                            } else {
                                p.setcantidadminjugadores(Integer.parseInt(args[2]));
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJugadores minimos configurado correctamente"));
                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes modificar sus jugadores minimos"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setminplayer <arena> <minplayers>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("tiempomax")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            p.setTiempomax(Integer.parseInt(args[2]));
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTiempo maximo de la partida configurado correctamente"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes modificar su tiempo maximos"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball tiempomax <arena> <tiempo>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setmainlobby")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    FileConfiguration config = plugin.getConfig();
                    Location l = jugador.getLocation();
                    config.set("MainLobby.x", l.getX() + "");
                    config.set("MainLobby.y", l.getY() + "");
                    config.set("MainLobby.z", l.getZ() + "");
                    config.set("MainLobby.Yaw", l.getYaw() + "");
                    config.set("MainLobby.Pitch", l.getPitch() + "");
                    config.set("MainLobby.world", l.getWorld().getName() + "");
                    plugin.saveConfig();
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLobby principal definida correctamente"));
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length >= 2) {
                    String nombre = args[1];
                    Partida p = plugin.getPartida(nombre);
                    if (p != null) {
                        if (p.estaActivada()) {
                            if (plugin.getPartidaJugador(jugador.getName()) == null) {
                                if (!p.estaIniciada()) {
                                    if (!p.estallena()) {
                                        PartidaManager.jugadorEntra(p, jugador, plugin);
                                    } else {
                                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida esta llena"));
                                    }
                                } else {
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida ya esta iniciada"));
                                }
                            } else {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYa estas en una partida"));
                                /*
                                System.out.println("hola");
                                if (messages.contains("BedWars.already-in-arena")) {
                                    String a=messages.getString("BedWars.already-in-arena");
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', a));
                                    
                                }
                                */
                            }

                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida esta desactivada"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball join <arena>"));
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                Partida p = plugin.getPartidaJugador(jugador.getName());
                if (p != null) {
                    PartidaManager.jugadorSale(p, jugador, plugin, false, false);
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo estas en ninguna partida"));
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            if (!partida.estaActivada()) {
                                if (partida.getLobby() == null) {
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes definir el lobby de la partida antes de activarla"));
                                    return true;
                                }
                                ArrayList<Equipo> equipos = partida.getequipos();
                                for (int i = 0; i < equipos.size(); i++) {
                                    if (equipos.get(i).getSpawn() == null) {
                                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes definir el spawn del equipo: " + equipos.get(i).gettipo() + " antes de activarla"));
                                        return true;
                                    }
                                }

                                partida.setEstado(EstadoPartida.ESPERANDO);
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida ha sido activada correctamente"));

                            } else {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida ya esta activada"));
                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe"));
                        }

                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball enable <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }

            } else if (args[0].equalsIgnoreCase("disable")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            if (!partida.estaActivada()) {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida ya esta desactivada"));
                                return true;
                            } else {
                                partida.setEstado(EstadoPartida.DESACTIVADA);
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida fue desactivada correctamente"));
                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball disable <arena>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("guardar")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            if (partida.getlimite1() != null && partida.getlimite2() != null) {
                                Manager.saveSchem(partida.getNombre(), partida.getlimite1().getBlockX(), partida.getlimite1().getBlockY(), partida.getlimite1().getBlockZ(), partida.getlimite2().getBlockX(), partida.getlimite2().getBlockY(), partida.getlimite2().getBlockZ(), partida.getlimite1().getWorld(), plugin);
                            } else {
                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDefine los limites de la partida"));
                            } //Logger.getLogger(Comando.class.getName()).log(Level.SEVERE, null, ex);
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball guardar <arena>"));
                    }
                }

            } else if (args[0].equalsIgnoreCase("cargar")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            try {
                                WorldCreator wc = new WorldCreator(nombre);

                                wc.environment(World.Environment.NORMAL);
                                wc.type(WorldType.FLAT);
                                wc.generatorSettings("0;0;0;");
                                wc.generateStructures(false);

                                World w1 = wc.createWorld();

                                Manager.loadSchem(partida.getNombre(), 10000, 100, 10000, w1, plugin);
                                World w = Bukkit.getWorld("nombre");
                                if (w != null) {
                                    w.setAnimalSpawnLimit(0);
                                    w.setAutoSave(false);
                                    w.setAmbientSpawnLimit(0);
                                    w.setMonsterSpawnLimit(0);
                                    w.setDifficulty(Difficulty.PEACEFUL);
                                    w.setStorm(false);
                                    w.setThundering(false);
                                    w.setTime(1000);
                                }

                                Location loc = new Location(w1, 10000, 100, 10000);
                                jugador.teleport(loc);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Comando.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball guardar <arena>"));
                    }
                }

            } else if (args[0].equalsIgnoreCase("setpos1")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.setlimite1(jugador.getLocation());
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("setpos2")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.setlimite2(jugador.getLocation());
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("setpos2")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.setlimite2(jugador.getLocation());
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("setcolor")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            ArrayList<Equipo> equipos = partida.getequipos();
                            for (int i = 0; i < equipos.size(); i++) {
                                if (equipos.equals(equipos.get(i).gettipo())) {
                                    partida.getequipos().get(i).setcolor(args[2]);
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cColor para el equipo: " + equipos.get(i).getcolor() + " definido correctamente"));
                                }
                            }
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("setemerald")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.addesmeralda(new Generador(jugador.getLocation(), partida, TipoGenerador.ESMERALDA));
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGenerador creado"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("setdiamond")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            partida.adddiamante(new Generador(jugador.getLocation(), partida, TipoGenerador.DIAMANTE));
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGenerador creado"));
                        }

                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                    }
                }
            } else if (args[0].equalsIgnoreCase("setgenerador")) {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null && args[2] != null) {
                            for (int i = 0; i < partida.getequipos().size(); i++) {
                                if (args[2].equals(partida.getequipos().get(i).gettipo())) {
                                    partida.addgeneradores(new Generador(jugador.getLocation(), partida, TipoGenerador.NORMAL, partida.getequipos().get(i)));
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGenerador creado"));
                                }
                            }

                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEsa partida no existe"));
                        }
                    }
                }
            } else {
                if (jugador.isOp() || jugador.hasPermission("bedwars.admin")) {
                    enviarMensajeAyudaAdmin(jugador);
                } else {
                    enviarMensajeAyudanormal(jugador);
                }
            }
            
        }
        return true;
    }

    

    public static void enviarMensajeAyudaAdmin(Player jugador) {
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAQUI VA TODA LA AYUDA DEL PLUGIN PARA ADMINS"));
    }

    public static void enviarMensajeAyudanormal(Player jugador) {
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAQUI VA TODA LA AYUDA DEL PLUGIN PARA USUARIOS NORMALES"));
    }

}
