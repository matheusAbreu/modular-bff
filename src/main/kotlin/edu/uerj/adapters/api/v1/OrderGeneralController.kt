package edu.uerj.adapters.api.v1

import edu.uerj.adapters.api.dto.ErrorDTO
import edu.uerj.adapters.api.dto.common.PaginatedResult
import edu.uerj.adapters.api.dto.order.OrderCompleteDTO
import edu.uerj.services.OrderService
import jakarta.inject.Inject
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Path("/v1/order")
class OrderGeneralController
    @Inject
    constructor(
        private val orderService: OrderService,
    ) {
        @Path("/{orderId}")
        @APIResponses(
            value = [
                APIResponse(
                    responseCode = "200",
                    description = "returned find Order",
                    content = [Content(schema = Schema(implementation = OrderCompleteDTO::class))],
                ),
                APIResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = [Content(schema = Schema(implementation = ErrorDTO::class))],
                ),
                APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = [Content(schema = Schema(implementation = ErrorDTO::class))],
                ),
            ],
        )
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        fun getOrderById(
            @PathParam("orderId") orderId: String,
        ): Response {
            try {
                val order =
                    orderService.getOrder(orderId.toLong()) ?: return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(ErrorDTO("Order with id $orderId not found"))
                        .build()

                return Response
                    .status(Response.Status.OK)
                    .entity(OrderCompleteDTO(order))
                    .build()
            } catch (exception: NumberFormatException) {
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO("Invalid orderId '$orderId'"))
                    .build()
            } catch (exception: Exception) {
                return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO(exception.message))
                    .build()
            }
        }

        @Path("/list")
        @APIResponses(
            value = [
                APIResponse(
                    responseCode = "200",
                    description = "returned list Orders",
                    content = [Content(schema = Schema(implementation = PaginatedResult::class))],
                ),
                APIResponse(
                    responseCode = "400",
                    description = "Error list orders",
                    content = [Content(schema = Schema(implementation = PaginatedResult::class))],
                ),
                APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = [Content(schema = Schema(implementation = ErrorDTO::class))],
                ),
            ],
        )
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        fun listOrders(
            @QueryParam("limitPerPage") @DefaultValue("30")
            limit: Long,
            @QueryParam("page") @DefaultValue("0")
            offset: Long,
        ): Response {
            try {
                var paginatedOrders = orderService.listAll(limit, offset)

                if (paginatedOrders.items == null) {
                    return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(PaginatedResult<OrderCompleteDTO>())
                        .build()
                }

                val paginedResult =
                    PaginatedResult(
                        items = paginatedOrders.items!!.map { OrderCompleteDTO(it) } as MutableList<OrderCompleteDTO>,
                        limit = paginatedOrders.limit,
                        total = paginatedOrders.total,
                    )
                return Response
                    .status(Response.Status.OK)
                    .entity(paginedResult)
                    .build()
            } catch (exception: NumberFormatException) {
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO("Invalid limit or offset"))
                    .build()
            } catch (exception: Exception) {
                return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO(exception.message))
                    .build()
            }
        }
    }
