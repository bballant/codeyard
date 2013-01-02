my.mean <- function(x) {
  return (sum(x) / length(x))
}

my.median <- function(x) {
  sorted.x <- sort(x)

  if (length(x) %% 2 == 0) {
    indeces <- c(length(x) / 2, length(x) / 2 + 1)
    return(mean(sorted.x[indeces]))
  } else {
    index <- ceiling(length(x) / 2)
    return(sorted.x[index])
  }
}

my.var <- function(x) {
  m <- mean(x)
  return(sum((x - m) ^ 2) / (length(x) - 1))
}

my.sd <- function(x) {
  return(sqrt(my.var(x)))
}

my.genderClassfication <- function() {
  data.file <- file.path('02.data', '01_heights_weights_genders.csv')
  heights.weights <- read.csv(data.file, header = TRUE, sep = ',')
  
  heights.weights <- transform(heights.weights, Male = ifelse(Gender == 'Male', 1, 0))
  logit.model <- glm(Male ~ Height + Weight, data = heights.weights, family = binomial(link = 'logit'))

  ggplot(heights.weights, aes(x = Weight, y = Height, color = Gender)) +
    geom_point() +
    geom_smooth() +
    stat_abline(
      intercept = coef(logit.model)[1] / coef(logit.model)[2],
      slope = - coef(logit.model)[3] / coef(logit.model)[2],
      geom = 'abline',
      color = 'black')
}
