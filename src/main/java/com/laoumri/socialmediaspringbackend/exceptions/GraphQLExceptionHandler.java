package com.laoumri.socialmediaspringbackend.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        if(ex instanceof EmailAddressAlreadyExists){
            return GraphQLError.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        if(ex instanceof RoleNotFoundException){
            return GraphQLError.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        if(ex instanceof AuthenticationException){
            return GraphQLError.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        if(ex instanceof InvalidRequestException){
            return GraphQLError.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        if(ex instanceof ResourceNotFoundException){
            return GraphQLError.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        if (ex instanceof AccessDeniedException) {
            System.out.println("Access denied");
            return GraphQLError.newError()
                    .message("You are not authorized to access this resource.")
                    .location(env.getField().getSourceLocation())
                    .path(env.getExecutionStepInfo().getPath())
                    .errorType(ErrorType.UNAUTHORIZED)
                    .build();
        }

        return super.resolveToSingleError(ex, env);
    }
}
