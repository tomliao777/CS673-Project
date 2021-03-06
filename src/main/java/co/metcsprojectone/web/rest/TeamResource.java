package co.metcsprojectone.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.metcsprojectone.domain.Team;

import co.metcsprojectone.repository.TeamRepository;
import co.metcsprojectone.repository.search.TeamSearchRepository;
import co.metcsprojectone.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    private static final String ENTITY_NAME = "team";

    private final TeamRepository teamRepository;

    private final TeamSearchRepository teamSearchRepository;

    public TeamResource(TeamRepository teamRepository, TeamSearchRepository teamSearchRepository) {
        this.teamRepository = teamRepository;
        this.teamSearchRepository = teamSearchRepository;
    }

    /**
     * POST  /teams : Create a new team.
     *
     * @param team the team to create
     * @return the ResponseEntity with status 201 (Created) and with body the new team, or with status 400 (Bad Request) if the team has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/teams")
    @Timed
    public ResponseEntity<Team> createTeam(@RequestBody Team team) throws URISyntaxException {
        log.debug("REST request to save Team : {}", team);
        if (team.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new team cannot already have an ID")).body(null);
        }
        Team result = teamRepository.save(team);
        teamSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teams : Updates an existing team.
     *
     * @param team the team to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated team,
     * or with status 400 (Bad Request) if the team is not valid,
     * or with status 500 (Internal Server Error) if the team couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/teams")
    @Timed
    public ResponseEntity<Team> updateTeam(@RequestBody Team team) throws URISyntaxException {
        log.debug("REST request to update Team : {}", team);
        if (team.getId() == null) {
            return createTeam(team);
        }
        Team result = teamRepository.save(team);
        teamSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, team.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teams : get all the teams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teams in body
     */
    @GetMapping("/teams")
    @Timed
    public List<Team> getAllTeams() {
        log.debug("REST request to get all Teams");
        return teamRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /teams/:id : get the "id" team.
     *
     * @param id the id of the team to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the team, or with status 404 (Not Found)
     */
    @GetMapping("/teams/{id}")
    @Timed
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        Team team = teamRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(team));
    }

    /**
     * DELETE  /teams/:id : delete the "id" team.
     *
     * @param id the id of the team to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamRepository.delete(id);
        teamSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/teams?query=:query : search for the team corresponding
     * to the query.
     *
     * @param query the query of the team search
     * @return the result of the search
     */
    @GetMapping("/_search/teams")
    @Timed
    public List<Team> searchTeams(@RequestParam String query) {
        log.debug("REST request to search Teams for query {}", query);
        return StreamSupport
            .stream(teamSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
