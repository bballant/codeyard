library(tm)
library(ggplot2)

spam.path <- "03.data/spam/"
spam2.path <- "03.data/spam_2/"
easyham.path <- "03.data/easy_ham/"
easyham2.path <- "03.data/easy_ham_2/"
hardham.path <- "03.data/hard_ham/"
hardham2.path <- "03.data/hard_ham_2/"

get.msg <- function(path) {
  con <- file(path, open="rt", encoding="latin1")
  text <- readLines(con)
  # message always begins after first full line break
  msg <- text[seq(which(text=="")[1]+1, length(text), 1)]
  close(con)
  return(paste(msg, collapse="\n"))
}

get.emailText <- function(path) {
  docs <- dir(path)
  docs <- docs[which(docs != "cmds")]
  all <- sapply(docs, function(p) get.msg(paste(path, p, sep="")))
  return(all)
}

# a term document matrix an an N x M matrix
# where the terms are the rows and documents are the columns
get.termDocumentMatrix <- function(doc.vec) {
  doc.corpus <- Corpus(VectorSource(doc.vec))
  control <- list(stopwords=TRUE, removePunctuation=TRUE, removeNumbers=TRUE, bounds=list(local = c(2,Inf))) 
  doc.tdm <- TermDocumentMatrix(doc.corpus, control)
  return(doc.tdm)
}

get.df <- function(path) {
  return(get.termFrequencyDataFrame(get.emailText(path)))
}

# a dataFrame describing stuff about the Term Frequency
get.termFrequencyDataFrame <- function(text) {
  email.matrix <- as.matrix(get.termDocumentMatrix(text))
  email.counts <- rowSums(email.matrix)
  email.df <- data.frame(
    cbind(names(email.counts), as.numeric(email.counts)),
    stringAsFactors=FALSE )
  names(email.df) <- c("term", "frequency")
  email.df$frequency <- as.numeric(email.df$frequency)

  # calculate the % of documents in which the given term occurs
  email.occurence <- sapply(1:nrow(email.matrix),
    function(i) {
      num <- length(which(email.matrix[i,] > 0))
      denom <- ncol(email.matrix)
      return(num / denom)
    } ) 

  # the frequency of each word within the entire corpus
  # i.e. what % of all words is this word?
  email.density <- email.df$frequency / sum(email.df$frequency)

  email.df <- transform(email.df, density=email.density,
    occurence=email.occurence )

  return(email.df)
}

dostuff <- function() {
  spam.df <- get.termFrequencyDataFrame(get.emailText(spam.path))
  easyham.df <- get.termFrequencyDataFrame(get.emailText(easyham.path))

  head(spam.df[with(spam.df, order(-occurence)),])
  head(easyham.df[with(easyham.df, order(-occurence)),])
}

classify.email <- function(path, training.df, prior=0.5, c=1e-6) {
  msg <- get.msg(path)
  msg.tdm <- get.termDocumentMatrix(msg)
  msg.freq <- rowSums(as.matrix(msg.tdm))
  # Find intersections of words
  msg.match <- intersect(names(msg.freq), training.df$term)
  if(length(msg.match) < 1) {
    return(prior*c^(length(msg.freq)))
  } else {
    match.probs <- training.df$occurrence[match(msg.match, training.df$term)]
    return(prior * prod(match.probs) * c^(length(msg.freq)-length(msg.match)))
  }
}

# the next 2 are identical
# tdm is term document matrix
get.emailClassification <- function(msg.tdm, training.df, prior=0.5, c=1e-6) {
  msg.freq <- rowSums(as.matrix(msg.tdm))
  msg.match <- intersect(names(msg.freq), training.df$term)
  if(length(msg.match) < 1) {
    return(prior*c^(length(msg.freq)))
  } else {
    match.probs <- training.df$occurrence[match(msg.match, training.df$term)]
    return(prior * prod(match.probs) * c^(length(msg.freq)-length(msg.match)))
  }    
}

classify.email2 <- function(msg.tdm, training.df, prior=0.5, c=1e-6) {
  msg.freq <- rowSums(as.matrix(msg.tdm))
  msg.match <- intersect(names(msg.freq), training.df$term)
  if(length(msg.match) < 1) {
    return(prior*c^(length(msg.freq)))
  } else {
    match.probs <- training.df$occurrence[match(msg.match, training.df$term)]
    return(prior * prod(match.probs) * c^(length(msg.freq)-length(msg.match)))
  }
}

test.spamham <- function() {
  spam.df <- get.termFrequencyDataFrame(get.emailText(spam.path))
  easyham.df <- get.termFrequencyDataFrame(get.emailText(easyham.path))

  hardham.docs <- dir(hardham.path)
  hardham.docs <- hardham.docs[which(hardham.docs != "cmds")]

  # hardham.tdms <- lapply(hardham.docs,
  #   function(p) get.termDocumentMatrix(get.msg(paste(hardham.path, p, sep=""))))

  # hardham.spamtest <- sapply(hardham.tdms,
  #   function(p) classify.email2(p, training.df=spam.df))

  # hardham.hamtest <- sapply(hardham.tdms,
  #   function(p) classify.email2(p, training.df=easyham.df))

  # hardham.spamtest <- sapply(hardham.docs,
  #   function(p) classify.email(paste(hardham.path, p, sep=""), training.df=spam.df))

  # hardham.hamtest <- sapply(hardham.docs,
  #   function(p) classify.email(paste(hardham.path, p, sep=""), training.df=easyham.df))
  
  hardham.tdms <- lapply(hardham.docs,
    function(p) get.termDocumentMatrix(get.msg(paste(hardham.path, p, sep=""))))

  hardham.spamtest <- sapply(hardham.tdms,
    function(p) get.emailClassification(p, training.df=spam.df))

  hardham.hamtest <- sapply(hardham.tdms,
    function(p) get.emailClassification(p, training.df=easyham.df))

  hardham.res <- ifelse(hardham.spamtest > hardham.hamtest, TRUE, FALSE)
  summary(hardham.res)
}

spam.classifier <- function(path) {
  pr.spam <- classify.email(path, spam.df)
  pr.ham <- classify.email(path, easyham.df)
  return(c(pr.spam, pr.ham, ifelse(pr.spam > pr.ham, 1, 0)))
}

