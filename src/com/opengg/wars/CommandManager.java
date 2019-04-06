package com.opengg.wars;

import com.opengg.core.GGInfo;
import com.opengg.core.network.NetworkEngine;
import com.opengg.core.network.Packet;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandManager {
    private static List<Command> clientCommandsCached = new ArrayList<>();
    private static HashMap<String, List<Consumer<Command>>> commandUsers = new HashMap<>();

    public static void initialize(){
        NetworkEngine.getReceiver().addProcessor(SimonWars.COMMAND_SEND_PACKET, CommandManager::parseReceivedCommands);
    }

    public static void sendCommand(Command command){
        clientCommandsCached.add(command);
    }

    public static void sendAllCommands(){
        if(clientCommandsCached.isEmpty()) return;

        var out = new GGOutputStream();
        try {
            out.write(clientCommandsCached.size());
            for(var command : clientCommandsCached){
                out.write(command.command);
                out.write(command.args.size());
                for(var arg : command.args){
                    out.write(arg);
                }
            }
            Packet.sendGuaranteed(NetworkEngine.getSocket(), SimonWars.COMMAND_SEND_PACKET, out.asByteArray(), NetworkEngine.getClient().getConnection());

            clientCommandsCached.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerCommandUser(String command, Consumer<Command> user){
        commandUsers.merge(command, List.of(user), (l1,l2) -> Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList()));
    }

    public static void parseReceivedCommands(Packet packet){
        var in = new GGInputStream(packet.getData());

        try {
            List<Command> commands = new ArrayList<>();
            int amount = in.readInt();
            for(int i = 0; i < amount; i++){
                String command = in.readString();
                int argAmount = in.readInt();
                List<String> args = new ArrayList<>();
                for(int j = 0; j < argAmount; j++){
                    args.add(in.readString());
                }

                var realCom = Command.create(command, args);
                commands.add(realCom);
            }
            runReceivedCommands(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runReceivedCommands(List<Command> commands){
        System.out.println(commands);
        for(var command : commands){
            var listeners = commandUsers.getOrDefault(command.command, List.of());
            listeners.forEach(c -> c.accept(command));
        }
    }


    public static void update(){
        if(SimonWars.offline){
            for(var command : clientCommandsCached){
                var listeners = commandUsers.getOrDefault(command.command, List.of());
                listeners.forEach(c -> c.accept(command));
            }
            clientCommandsCached.clear();
        }else if(!GGInfo.isServer()){
            sendAllCommands();
        }


    }
}
