/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7;

import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Partida;
import com.gmail.cuestamanue7.managers.PartidaManager;
import com.gmail.cuestamanue7.worldedit.Manager;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

    private Paintball plugin;

    public Comando(Paintball plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player jugador = (Player) sender;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                //paintball create <arena>
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        FileConfiguration config = plugin.getConfig();
                        if (plugin.getPartidas() == null && config.contains("MainLobby")) {
                            Partida partida = new Partida(nombre);
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    plugin.reloadConfig();
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cConfig recargada correctamente"));
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setlobby")) {
                //paintball setlobby <nombre>
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setlobby <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setspawnteam1")) {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            p.getEquipo1().setSpawn(jugador.getLocation().clone());
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSpawn para el equipo 1 definido correctamente"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes ponerle un spawn"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setspawnteam1 <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }

            } else if (args[0].equalsIgnoreCase("setspawnteam2")) {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            p.getEquipo2().setSpawn(jugador.getLocation().clone());
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSpawn para el equipo 2 definido correctamente"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes ponerle un spawn"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setspawnteam2 <arena>"));
                    }

                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }

            } else if (args[0].equalsIgnoreCase("setnameteam1")) {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            p.getEquipo1().settipo(args[2]);
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
            } else if (args[0].equalsIgnoreCase("setnameteam2")) {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 3) {
                        String nombre = args[1];
                        Partida p = plugin.getPartida(nombre);
                        if (p != null) {
                            p.getEquipo2().settipo(args[2]);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEquipo con nombre: " + args[2] + " configurado correctamente"));
                        } else {
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida no existe, no puedes modificar el nombre del equipo"));
                        }
                    } else {
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes usar el comando /paintball setnameteam2 <arena> <nombreEquipo>"));
                    }
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para ejecutar este comando"));
                }
            } else if (args[0].equalsIgnoreCase("setmaxplayers")) {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                Partida partida = plugin.getPartidaJugador(jugador.getName());
                if (partida != null) {
                    PartidaManager.jugadorSale(partida, jugador, plugin, false, false);
                } else {
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo estas en ninguna partida"));
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            if (!partida.estaActivada()) {
                                if (partida.getLobby() == null) {
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes definir el lobby de la partida antes de activarla"));
                                    return true;
                                }
                                if (partida.getEquipo1().getSpawn() == null) {
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes definir el spawn del equipo 1 antes de activarla"));
                                    return true;
                                }
                                if (partida.getEquipo2().getSpawn() == null) {
                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDebes definir el spawn del equipo 2 antes de activarla"));
                                    return true;
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    if (args.length >= 2) {
                        String nombre = args[1];
                        Partida partida = plugin.getPartida(nombre);
                        if (partida != null) {
                            try {
                                Manager.loadSchem(partida.getNombre(), jugador.getLocation().getBlockX(), jugador.getLocation().getBlockY(), jugador.getLocation().getBlockZ(), jugador.getWorld(), plugin, jugador);
                            } catch (MaxChangedBlocksException ex) {
                                Logger.getLogger(Comando.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Comando.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (DataException ex) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
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
            } else {
                if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                    enviarMensajeAyudaAdmin(jugador);
                } else {
                    enviarMensajeAyudanormal(jugador);
                }
            }

        } else {
            if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
                enviarMensajeAyudaAdmin(jugador);
            } else {
                enviarMensajeAyudanormal(jugador);
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
