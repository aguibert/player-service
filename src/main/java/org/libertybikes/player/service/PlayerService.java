package org.libertybikes.player.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class PlayerService {

	Map<String, Player> allPlayers = new HashMap<>();

	@PostConstruct
	public void initPlayers() {
		for (int i = 0; i < 10; i++)
			createPlayer();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Player> getPlayers() {
		return allPlayers.values();
	}

	@GET
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public String createPlayer() {
		Player p = new Player();
		p.id = UUID.randomUUID().toString();
		p.name = "Bob";
		p.wins = new Random().nextInt(100);
		allPlayers.put(p.id, p);
		return p.id;
	}

	@GET
	@Path("/{playerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Player getPlayerById(@PathParam("playerId") String id) {
		return allPlayers.get(id);
	}

	@GET
	@Path("/{playerId}/win")
	@Produces(MediaType.APPLICATION_JSON)
	public int playerWon(@PathParam("playerId") String id) {
		Player p = allPlayers.get(id);
		if (p == null)
			return -1;
		return ++p.wins;
	}

	@GET
	@Path("/ranks")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getTopPlayers() {
		return allPlayers.values().stream()//
				.sorted((p1, p2) -> p2.wins - p1.wins)//
				.limit(3)//
				.collect(Collectors.toList());
	}

}
