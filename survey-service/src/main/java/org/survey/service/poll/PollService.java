package org.survey.service.poll;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.survey.model.poll.Poll;

/**
 * PollServiceImpl is an WebService implementation of PollService. Can not use
 * delegate design pattern, since PollRepository uses overloaded methods, and
 * apparently WS-I profile does not support them. Even so, it might be better to
 * reveal only some of the PollRepository methods as WebService. Also,
 * WebService does not support lists without extensions. WebParam annotations
 * are not necessary, but adds proper names to parameters in WSDL.
 */
@WebService
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("/polls")
public interface PollService {
    /**
     * Returns all polls from repository.
     */
    @GET
    Poll[] findAll();

    /**
     * Creates a poll to repository.
     * 
     * @throws NullPointerException
     *             if poll is null.
     * @throws IllegalArgumentException
     *             if poll with same name already exists
     */
    @POST
    Poll create(@WebParam(name = "poll") Poll poll);
    
    /**
     * Updates a poll in repository.
     */
    @PUT
    Poll update(@WebParam(name = "poll") Poll poll);
    
    /**
     * Returns a poll by id.
     */
    @GET
    @Path("/{id}")
    Poll findOne(@PathParam("id") @WebParam(name = "id") Long id);
    /**
     * Returns a poll by name.
     */
    @GET
    @Path("/name/{name}")
    Poll findByName(@PathParam("name") @WebParam(name = "name") String name);
    
    @GET
    @Path("/username/{username}")
    Poll[] findByOwner(@PathParam("username") @WebParam(name = "username") String username);

    /**
     * Returns true if a poll by id exists.
     */
    @GET
    @Path("/exists/{id}")
    boolean exists(@PathParam("id") @WebParam(name = "id") Long id);

    /**
     * Deletes a poll by id.
     */
    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") @WebParam(name = "id") Long id);

    /**
     * Returns the count of polls in repository.
     */
    @GET
    @Path("/count")
    long count();
}
