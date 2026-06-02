package org.acme.adapters.api.v1

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/v1/order")
class OrderController {

    @Path("/{orderId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getOrderById(
        @PathParam("orderId") orderId: String,
    ): Response {
        return try {
            Response.ok().entity("Order ID: $orderId").build()
        } catch (exception: Exception){
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exception.message!!).build()
        }
    }
}