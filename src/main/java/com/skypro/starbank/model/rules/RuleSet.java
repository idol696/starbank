package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rule_sets")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id","product_name","product_id","product_text","rules"})
@Schema(description = "RuleSet entity representing a product's rules")
public class RuleSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the rule set", example = "1")
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true, columnDefinition = "UUID")
    @Schema(description = "Product ID (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @JsonProperty("product_id")
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    @Schema(description = "Product Name", example = "Invest 500")
    @JsonProperty("product_name")
    private String productName;

    @Column(name = "product_text", columnDefinition = "TEXT")
    @Schema(description = "Product Description", example = "Investment details...")
    @JsonProperty("product_text")
    private String productText;

    @OneToMany(mappedBy = "ruleSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonProperty("rules")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Rule> rules;

    public RuleSet() {}

    public RuleSet(UUID productId, String productName, String productText, List<Rule> rules) {
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.rules = rules;
    }

    public Long getId() { return id; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public List<Rule> getRules() { return rules; }
    public void setRules(List<Rule> rules) { this.rules = rules; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSet ruleSet = (RuleSet) o;
        return Objects.equals(id, ruleSet.id) && Objects.equals(productId, ruleSet.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productText='" + productText + '\'' +
                ", rules=" + rules +
                '}';
    }
}
