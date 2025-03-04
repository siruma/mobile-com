package com.homework

import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.Builder()
  .serverUrl("https://api.oulunliikenne.fi/proxy/graphql")
  .build()